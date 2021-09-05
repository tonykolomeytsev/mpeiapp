package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.onPageSelected
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.NeedToUpdate
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.databinding.FragmentScheduleBinding
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule.main.adapter.WeeksScrollHelper
import kekmech.ru.feature_schedule.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent.Wish
import kekmech.ru.feature_schedule.main.elm.ScheduleFeatureFactory
import kekmech.ru.feature_schedule.main.elm.ScheduleState
import kekmech.ru.feature_schedule.main.item.*
import org.koin.android.ext.android.inject
import java.time.LocalDate

private const val WEEK_MIN_NUMBER = 0
private const val WEEK_MAX_NUMBER = 17
private const val FAB_ANIMATION_DURATION = 100L
private const val DAYS_TO_VIEW = 6
internal const val SHIMMER_ITEM_ID = 0

@Suppress("TooManyFunctions")
internal class ScheduleFragment :
    BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState>(),
    ActivityResultListener,
    NeedToUpdate {

    override val initEvent = Wish.Init
    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()
    private val viewPagerAdapter by fastLazy { createViewPagerAdapter() }
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }
    private val analytics by screenAnalytics("Schedule")
    private val viewBinding by viewBinding(FragmentScheduleBinding::bind)

    // for viewPager sliding debounce
    private var viewPagerPositionToBeSelected: Int? = null

    // for viewPager callback issue
    private var viewPagerFirstSelectionIgnored: Boolean = false

    override fun createStore() = inject<ScheduleFeatureFactory>().value.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            weeksScrollHelper.attach(recyclerView)
            recyclerView.adapter = weeksScrollAdapter
            recyclerView.itemAnimator = null
            recyclerView.setHasFixedSize(true)
            recyclerView.addScrollAnalytics(analytics, "WeeksRecyclerView")

            viewPager.adapter = viewPagerAdapter
            viewPager.onPageSelected {
                if (viewPagerFirstSelectionIgnored) feature.accept(Wish.Action.PageChanged(it))
                viewPagerFirstSelectionIgnored = true
            }
            viewPager.addScrollAnalytics(analytics, "WorkingDaysViewPager")

            appBarLayout.outlineProvider = null
            appBarLayout.addSystemTopPadding()
            fab.setOnClickListener { feature.accept(Wish.Click.FAB) }
        }
    }

    override fun onDestroyView() {
        weeksScrollHelper.detach(viewBinding.recyclerView)
        super.onDestroyView()
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.NavigateToNoteList -> {
            dependencies.notesFeatureLauncher.launchNoteList(
                selectedClasses = effect.classes,
                selectedDate = effect.date,
                resultKey = NOTES_LIST_RESULT_KEY
            )
            setResultListener<EmptyResult>(NOTES_LIST_RESULT_KEY) {
                feature.accept(Wish.Action.NotesUpdated)
            }
        }
    }

    override fun render(state: ScheduleState) {
        viewPagerAdapter.update(ScheduleClassesListConverter.map(state))
        renderStatusBar(state)
        renderWeekCalendar(state)
        renderViewPager(state)
        renderFloatingActionButton(state)
    }

    private fun renderStatusBar(state: ScheduleState) = with(viewBinding) {
        textViewHeader.text = getFormattedDay(state.selectedDate)
        textViewDescription.text = state.weekOfSemester?.let { getFormattedWeek(it) }.orEmpty()
    }

    private fun renderViewPager(state: ScheduleState) = with(viewBinding) {
        if (viewPagerPositionToBeSelected != state.selectedDate.dayOfWeek.value) {
            val smoothScroll = viewPagerPositionToBeSelected != null
            viewPagerPositionToBeSelected = state.selectedDate.dayOfWeek.value
            val selectedItem = state.selectedDate.dayOfWeek.value - 1
            if (selectedItem != viewPager.currentItem) viewPager.post {
                if (selectedItem in 0 until DAYS_TO_VIEW) viewPager.setCurrentItem(selectedItem, smoothScroll)
            }
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
            viewBinding.fab.setText(R.string.schedule_fab_next)
            viewBinding.fab.setIconResource(R.drawable.ic_next_schedule)
        } else {
            viewBinding.fab.setText(R.string.schedule_fab_prev)
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
        WorkingDayAdapterItem(DAY_OF_WEEK_MONDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_TUESDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_WEDNESDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_THURSDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_FRIDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_SATURDAY, ::onClassesClick, ::onClassesScroll)
    )

    private fun onClassesClick(classes: Classes) {
        analytics.sendClick("ClickClasses")
        feature.accept(Wish.Click.Classes(classes))
    }

    private fun onClassesScroll(dy: Int) {
        feature.accept(Wish.Action.ClassesScrolled(dy))
    }

    private fun createWeekDaysHelper() = WeeksScrollHelper(
        onWeekSelectListener = {
            analytics.sendClick("Week_$it")
            feature.accept(Wish.Action.SelectWeek(it))
        }
    )

    private fun createWeekScrollAdapter() = WeeksScrollAdapter(
        WeekAdapterItem(
            onDayClickListener = {
                analytics.sendClick("Day_${it.date}")
                feature.accept(Wish.Click.Day(it.date))
            }
        )
    )

    override fun onUpdate() {
        feature.accept(Wish.Action.UpdateScheduleIfNeeded)
    }

    private fun getFormattedDay(day: LocalDate): String {
        val dayOfWeekName = requireContext()
            .getStringArray(R.array.days_of_week)
            .getOrElse(day.dayOfWeek.value - 1) { "" }
        val dayOfMonthName = requireContext()
            .getStringArray(R.array.months)
            .getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $dayOfMonthName"
    }

    private fun getFormattedWeek(weekNumber: Int): String {
        if (weekNumber in WEEK_MIN_NUMBER..WEEK_MAX_NUMBER) {
            return requireContext().getString(R.string.schedule_semester_week, weekNumber)
        } else {
            return requireContext().getString(R.string.schedule_weekend_week)
        }
    }

    companion object {

        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
    }
}