package kekmech.ru.feature_schedule_impl.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kekmech.ru.ext_android.ActivityResultListener
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.getStringArray
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.views.onPageSelected
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.databinding.FragmentScheduleBinding
import kekmech.ru.feature_schedule_impl.di.ScheduleDependencies
import kekmech.ru.feature_schedule_impl.presentation.screen.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule_impl.presentation.screen.main.adapter.WeeksScrollHelper
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent.Ui
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreProvider
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_FRIDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_MONDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_SATURDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_THURSDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_TUESDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.DAY_OF_WEEK_WEDNESDAY
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.WeekAdapterItem
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.WorkingDayAdapterItem
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.addScrollAnalytics
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm.BaseFragment
import kekmech.ru.lib_navigation.features.TabScreenStateSaver
import kekmech.ru.lib_navigation.features.TabScreenStateSaverImpl
import org.koin.android.ext.android.inject
import java.time.LocalDate
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

@Suppress("TooManyFunctions")
internal class ScheduleFragment :
    BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState>(R.layout.fragment_schedule),
    ActivityResultListener,
    TabScreenStateSaver by TabScreenStateSaverImpl("schedule") {

    private val dependencies by inject<ScheduleDependencies>()
    private val viewPagerAdapter by fastLazy { createViewPagerAdapter() }
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }
    private val analytics by screenAnalytics("Schedule")
    private val viewBinding by viewBinding(FragmentScheduleBinding::bind)

    // for viewPager sliding debounce
    private var viewPagerPositionToBeSelected: Int? = null

    override fun createStore() = inject<ScheduleStoreProvider>().value.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            weeksScrollHelper.attach(recyclerView)
            recyclerView.adapter = weeksScrollAdapter
            recyclerView.itemAnimator = null
            recyclerView.setHasFixedSize(true)
            recyclerView.addScrollAnalytics(analytics, "WeeksRecyclerView")

            viewPager.adapter = viewPagerAdapter
            viewPager.setCurrentItem(getSavedViewPagerCurrentItem(), false)
            viewPager.onPageSelected { store.accept(Ui.Action.PageChanged(it)) }
            viewPager.addScrollAnalytics(analytics, "WorkingDaysViewPager")

            appBarLayout.outlineProvider = null
            appBarLayout.addSystemTopPadding()
            fab.setOnClickListener { store.accept(Ui.Click.FAB) }
        }
    }

    override fun onDestroyView() {
        saveViewPagerState()
        viewBinding.viewPager.adapter = null
        weeksScrollHelper.detach(viewBinding.recyclerView)
        super.onDestroyView()
    }

    override fun handleEffect(effect: ScheduleEffect) =
        when (effect) {
            is ScheduleEffect.NavigateToNoteList -> {
                parentFragment?.setResultListener<EmptyResult>(NOTES_LIST_RESULT_KEY) {
                    store.accept(Ui.Action.NotesUpdated)
                }
                dependencies.notesFeatureLauncher.launchNoteList(
                    selectedClasses = effect.classes,
                    selectedDate = effect.date,
                    resultKey = NOTES_LIST_RESULT_KEY
                )
            }
        }

    override fun render(state: ScheduleState) {
        viewPagerAdapter.update(ScheduleClassesListConverter.map(state))
        renderStatusBar(state)
        renderWeekCalendar(state)
        renderViewPager(state)
        renderFloatingActionButton(state)
    }

    private fun renderStatusBar(state: ScheduleState) =
        with(viewBinding) {
            textViewHeader.text = getFormattedDay(state.selectedDate)
            textViewDescription.text = state.weekOfSemester?.let(::getFormattedWeek).orEmpty()
        }

    private fun renderViewPager(state: ScheduleState) =
        with(viewBinding) {
            val newSelectedPosition = state.selectedDate.dayOfWeek.value - 1
            if (viewPagerPositionToBeSelected != newSelectedPosition &&
                viewPager.currentItem != newSelectedPosition &&
                newSelectedPosition in 0 until DAYS_TO_VIEW
            ) {
                viewPager.setCurrentItem(newSelectedPosition, viewPagerPositionToBeSelected != null)
                viewPagerPositionToBeSelected = newSelectedPosition
            }
        }

    private fun renderWeekCalendar(state: ScheduleState) {
        weeksScrollAdapter.selectDay(state.selectedDate) { position ->
            with(viewBinding.recyclerView) {
                scrollToPosition(position)
            }
        }
    }

    private fun renderFloatingActionButton(state: ScheduleState) {
        if (!state.appSettings.showNavigationButton) {
            viewBinding.fab.isVisible = false
            return
        } else {
            viewBinding.fab.isVisible = true
        }
        if (state.isOnCurrentWeek) {
            viewBinding.fab.setText(Strings.schedule_fab_next)
            viewBinding.fab.setIconResource(R.drawable.ic_next_schedule)
        } else {
            viewBinding.fab.setText(Strings.schedule_fab_prev)
            viewBinding.fab.setIconResource(R.drawable.ic_prev_schedule)
        }
        if (state.isNavigationFabVisible) with(viewBinding.fab) {
            clearAnimation()
            isEnabled = true
            animate()
                .alpha(1f)
                .setDuration(FAB_ANIMATION_DURATION)
                .start()
        } else with(viewBinding.fab) {
            clearAnimation()
            isEnabled = false
            animate()
                .alpha(0f)
                .setDuration(FAB_ANIMATION_DURATION)
                .start()
        }
    }

    private fun createViewPagerAdapter() = BaseAdapter(
        WorkingDayAdapterItem(
            DAY_OF_WEEK_MONDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        ),
        WorkingDayAdapterItem(
            DAY_OF_WEEK_TUESDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        ),
        WorkingDayAdapterItem(
            DAY_OF_WEEK_WEDNESDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        ),
        WorkingDayAdapterItem(
            DAY_OF_WEEK_THURSDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        ),
        WorkingDayAdapterItem(
            DAY_OF_WEEK_FRIDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        ),
        WorkingDayAdapterItem(
            DAY_OF_WEEK_SATURDAY,
            ::onClassesClick,
            ::onClassesScroll,
            ::onReloadClick
        )
    )

    private fun onClassesClick(classes: Classes) {
        analytics.sendClick("ClickClasses")
        store.accept(Ui.Click.Classes(classes))
    }

    private fun onClassesScroll(dy: Int) {
        store.accept(Ui.Action.ClassesScrolled(dy))
    }

    private fun onReloadClick() {
        analytics.sendClick("ClassesReload")
        store.accept(Ui.Click.Reload)
    }

    private fun createWeekDaysHelper() = WeeksScrollHelper(
        onWeekSelectListener = {
            analytics.sendClick("Week_$it")
            store.accept(Ui.Action.SelectWeek(it))
        }
    )

    private fun createWeekScrollAdapter() = WeeksScrollAdapter(
        WeekAdapterItem(
            onDayClickListener = {
                analytics.sendClick("Day_${it.date}")
                store.accept(Ui.Click.Day(it.date))
            }
        )
    )

    private fun getFormattedDay(day: LocalDate): String {
        val dayOfWeekName = requireContext()
            .getStringArray(StringArrays.days_of_week)
            .getOrElse(day.dayOfWeek.value - 1) { "" }
        val dayOfMonthName = requireContext()
            .getStringArray(StringArrays.months)
            .getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $dayOfMonthName"
    }

    private fun getFormattedWeek(weekOfSemester: WeekOfSemester): String {
        return when (weekOfSemester) {
            is WeekOfSemester.Studying -> requireContext()
                .getString(Strings.schedule_semester_week, weekOfSemester.num)
            is WeekOfSemester.NonStudying -> requireContext().getString(Strings.schedule_weekend_week)
        }
    }

    private fun getSavedViewPagerCurrentItem(): Int =
        stateBundle.getInt("selected_page")

    private fun saveViewPagerState() {
        stateBundle.putInt("selected_page", viewBinding.viewPager.currentItem)
    }

    companion object {

        private const val FAB_ANIMATION_DURATION = 100L
        private const val DAYS_TO_VIEW = 6

        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
    }
}
