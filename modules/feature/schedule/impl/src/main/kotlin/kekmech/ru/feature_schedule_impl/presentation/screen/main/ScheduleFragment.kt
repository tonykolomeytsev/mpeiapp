package kekmech.ru.feature_schedule_impl.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.addSystemBottomMargin
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.getStringArray
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.notNullSerializable
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.databinding.FragmentScheduleBinding
import kekmech.ru.feature_schedule_impl.di.ScheduleDependencies
import kekmech.ru.feature_schedule_impl.presentation.screen.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule_impl.presentation.screen.main.adapter.WeeksScrollHelper
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDaysAction
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDaysPager
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent.Ui
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.WeekAdapterItem
import kekmech.ru.lib_analytics_android.addScrollAnalytics
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.features.TabScreenStateSaver
import kekmech.ru.lib_navigation.features.TabScreenStateSaverImpl
import kekmech.ru.ui_theme.theme.MpeixTheme
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

@Suppress("TooManyFunctions")
internal class ScheduleFragment :
    Fragment(R.layout.fragment_schedule),
    ElmRendererDelegate<ScheduleEffect, ScheduleState>,
    TabScreenStateSaver by TabScreenStateSaverImpl("schedule") {

    private val dependencies by inject<ScheduleDependencies>()
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }
    private val analytics by screenAnalytics("Schedule")
    private val viewBinding by viewBinding(FragmentScheduleBinding::bind)

    // Compose state of the days pager, updated in render()
    private var dayPages by mutableStateOf(ScheduleClassesListConverter.loadingPages)
    private var selectedDayPage by mutableIntStateOf(-1)
    private var savedScheduleBannerText by mutableStateOf<String?>(null)

    // Last page the days pager settled on, saved to the TabScreenStateSaver
    private var currentDayPage = 0

    private val store by androidElmStore(
        viewModelStoreOwner = { requireActivity() },
        savedStateRegistryOwner = { requireActivity() },
    ) { inject<ScheduleStoreFactory>().value.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Подписываемся заранее, а не при открытии заметок: фрагмент пересоздается каждый раз,
        // когда возвращаемся на таб, и результат ждет в FragmentManager'е активити
        setResultListener<EmptyResult>(
            key = NOTES_LIST_RESULT_KEY,
            resultMapper = Bundle::notNullSerializable,
        ) {
            store.accept(Ui.Action.NotesUpdated)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            weeksScrollHelper.attach(recyclerView)
            recyclerView.adapter = weeksScrollAdapter
            recyclerView.itemAnimator = null
            recyclerView.setHasFixedSize(true)
            recyclerView.addScrollAnalytics(analytics, "WeeksRecyclerView")

            // Apply the current state before the first composition,
            // so the pager opens on the selected day instead of scrolling to it
            renderDaysPager(store.states.value)
            currentDayPage = selectedDayPage
                .takeIf { it in 0 until DAY_PAGES_COUNT }
                ?: getSavedViewPagerCurrentItem()
            daysPager.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            daysPager.setContent {
                MpeixTheme {
                    ScheduleDaysPager(
                        pages = dayPages,
                        selectedPage = selectedDayPage,
                        onAction = ::onDaysPagerAction,
                        savedScheduleBannerText = savedScheduleBannerText,
                        initialPage = currentDayPage,
                    )
                }
            }

            appBarLayout.outlineProvider = null
            appBarLayout.addSystemTopPadding()
            fab.setOnClickListener { store.accept(Ui.Click.FAB) }
            fab.addSystemBottomMargin()
        }
    }

    override fun onResume() {
        store.accept(Ui.Action.UpdateScheduleIfNeeded)
        super.onResume()
    }

    override fun onDestroyView() {
        saveViewPagerState()
        weeksScrollHelper.detach(viewBinding.recyclerView)
        super.onDestroyView()
    }

    override fun handleEffect(effect: ScheduleEffect) =
        when (effect) {
            is ScheduleEffect.NavigateToNoteList -> {
                dependencies.notesFeatureLauncher.launchNoteList(
                    selectedClasses = effect.classes,
                    selectedDate = effect.date,
                    resultKey = NOTES_LIST_RESULT_KEY
                )
            }
        }

    override fun render(state: ScheduleState) {
        renderDaysPager(state)
        renderStatusBar(state)
        renderWeekCalendar(state)
        renderFloatingActionButton(state)
    }

    private fun renderStatusBar(state: ScheduleState) =
        with(viewBinding) {
            textViewHeader.text = getFormattedDay(state.selectedDate)
            textViewDescription.text = state.weekOfSemester?.let(::getFormattedWeek).orEmpty()
        }

    private fun renderDaysPager(state: ScheduleState) {
        dayPages = ScheduleClassesListConverter.map(state)
        // Out of range pages (Sunday) are ignored by the pager
        selectedDayPage = state.selectedDate.dayOfWeek.value - 1
        renderSavedScheduleBanner(state)
    }

    private fun renderSavedScheduleBanner(state: ScheduleState) {
        savedScheduleBannerText = if (state.isShowingSavedSchedule) {
            val updatedAt = state.selectedResource?.updatedAt?.let(::getFormattedUpdatedAt)
            getString(Strings.schedule_saved_banner_text, updatedAt.orEmpty())
        } else {
            null
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

    private fun onDaysPagerAction(action: ScheduleDaysAction) {
        when (action) {
            is ScheduleDaysAction.PageSelected -> {
                currentDayPage = action.page
                store.accept(Ui.Action.PageChanged(action.page))
            }
            is ScheduleDaysAction.PagerDragged -> analytics.sendScroll("WorkingDaysViewPager")
            is ScheduleDaysAction.ClassesScrolled -> store.accept(Ui.Action.ClassesScrolled(action.dy))
            is ScheduleDaysAction.ClassesClick -> {
                analytics.sendClick("ClickClasses")
                store.accept(Ui.Click.Classes(action.classes))
            }
            is ScheduleDaysAction.LunchClick -> requireContext().openLinkExternal("mpeix://map?tab=FOOD")
            is ScheduleDaysAction.SavedScheduleBannerClick -> {
                analytics.sendClick("SavedScheduleBannerRefresh")
                store.accept(Ui.Click.SavedScheduleBanner)
            }
            is ScheduleDaysAction.ReloadClick -> {
                analytics.sendClick("ClassesReload")
                store.accept(Ui.Click.Reload)
            }
        }
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

    private fun getFormattedUpdatedAt(updatedAt: Instant): String =
        DateTimeFormatter
            .ofPattern("d MMMM, HH:mm", resources.configuration.locales[0])
            .withZone(ZoneId.systemDefault())
            .format(updatedAt)

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
        stateBundle.putInt("selected_page", currentDayPage)
    }

    companion object {

        private const val FAB_ANIMATION_DURATION = 100L

        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
    }
}
