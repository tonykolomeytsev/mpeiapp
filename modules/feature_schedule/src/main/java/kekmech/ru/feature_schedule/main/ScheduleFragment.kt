package kekmech.ru.feature_schedule.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_android.ActivityResultListener
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.viewbinding.unit
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.NeedToUpdate
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.databinding.FragmentScheduleBinding
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule.main.helpers.WeeksScrollHelper
import kekmech.ru.feature_schedule.main.item.*
import kekmech.ru.feature_schedule.main.presentation.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import org.koin.android.ext.android.inject
import java.time.LocalDate

private const val REQUEST_CODE_NOTES_UPDATED = 9328

internal class ScheduleFragment : BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState, ScheduleFeature>(),
    ActivityResultListener,
    NeedToUpdate {

    init { retainInstance = true }

    override val initEvent = Wish.Init

    override fun createFeature() = inject<ScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()

    private val viewPagerAdapter by fastLazy { createViewPagerAdapter() }
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }

    private val analytics: ScheduleAnalytics by inject()
    private val viewBinding by viewBinding(FragmentScheduleBinding::bind)

    // for viewPager sliding debounce
    private var viewPagerPositionToBeSelected: Int? = null
    private var weekOffsetToBeSelected: Int = 0

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        viewBinding.apply {
            weeksScrollHelper.attach(recyclerView, weekOffsetToBeSelected)
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = weeksScrollAdapter
            recyclerView.itemAnimator = null
            recyclerView.setHasFixedSize(true)
            recyclerView.addScrollAnalytics(analytics, "WeeksRecyclerView")
            viewPager.adapter = viewPagerAdapter
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    feature.accept(Wish.Action.OnPageChanged(position))
                }
            })
            viewPager.addScrollAnalytics(analytics, "WorkingDaysViewPager")
            appBarLayout.outlineProvider = null
            appBarLayout.addSystemTopPadding()
            fab.setOnClickListener { feature.accept(Wish.Click.OnFAB) }
        }
        analytics.sendScreenShown()
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.NavigateToNoteList -> {
            dependencies.notesFeatureLauncher
                .launchNoteList(effect.classes, effect.date, parentFragment, REQUEST_CODE_NOTES_UPDATED)
        }
    }

    override fun render(state: ScheduleState) = viewBinding.unit {
        weekOffsetToBeSelected = state.weekOffset
        appBarLayoutGroup.isVisible = !state.isLoading

        textViewHeader.text = getFormattedDay(state.selectedDay.date)
        textViewDescription.text = state.weekOfSemester?.let { getFormattedWeek(it) }.orEmpty()

        renderWeekSelection(state)
        weeksScrollAdapter.update(state.weekItems)
        weeksScrollAdapter.selectDay(state.selectedDay)
        viewPagerAdapter.update(ScheduleClassesListConverter.map(state))

        renderShimmer(state)
        renderFloatingActionButton(state)

        if (viewPagerPositionToBeSelected != state.selectedDay.dayOfWeek) {
            val smoothScroll = viewPagerPositionToBeSelected != null
            viewPagerPositionToBeSelected = state.selectedDay.dayOfWeek
            val selectedItem = state.selectedDay.dayOfWeek - 1
            if (selectedItem != viewPager.currentItem) viewPager.post {
                if (selectedItem in 0..5) viewPager.setCurrentItem(selectedItem, smoothScroll)
            }
        }
    }

    private fun renderWeekSelection(state: ScheduleState) {
        val position = state.weekOffset + (Int.MAX_VALUE / 2)
        viewBinding.recyclerView.scrollToPosition(position)
    }

    private fun renderShimmer(state: ScheduleState) {
//        if (state.selectedWeekSchedule == null) {
//            viewBinding.shimmerViewContainer.visibility = View.VISIBLE
//            viewBinding.shimmerViewContainer.startShimmer()
//        } else {
//            viewBinding.shimmerViewContainer.visibility = View.INVISIBLE
//            viewBinding.shimmerViewContainer.stopShimmer()
//        }
        //viewBinding.shimmerViewContainer.visibility = View.VISIBLE
        //viewBinding.shimmerViewContainer.startShimmer()
    }

    private fun renderFloatingActionButton(state: ScheduleState) {
        if (!state.appSettings.showNavigationButton) {
            viewBinding.fab.isVisible = false
            return
        } else {
            viewBinding.fab.isVisible = true
        }
        if (state.isNavigationFabCurrentWeek) {
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
                .setDuration(100L)
                .start()
        } else with(viewBinding.fab) {
            clearAnimation()
            isEnabled = false
            animate()
                .alpha(0f)
                .setDuration(100L)
                .start()
        }
    }

    private fun createViewPagerAdapter() = BaseAdapter(
        WorkingDayAdapterItem(DAY_OF_WEEK_MONDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_TUESDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_WEDNESDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_THURSDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_FRIDAY, ::onClassesClick, ::onClassesScroll),
        WorkingDayAdapterItem(DAY_OF_WEEK_SATURDAY, ::onClassesClick, ::onClassesScroll),
        ClassesShimmerAdapterItem()
    )

    private fun onClassesClick(classes: Classes) {
        analytics.sendClick("Classes")
        feature.accept(Wish.Click.OnClassesClick(classes))
    }

    private fun onClassesScroll(dy: Int) {
        feature.accept(Wish.Action.OnClassesScroll(dy))
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
                feature.accept(Wish.Click.OnDayClick(it))
            }
        )
    )

    override fun onUpdate() {
        if (isResumed && !isRemoving) {
            try {
                feature.accept(Wish.Action.UpdateScheduleIfNeeded)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_NOTES_UPDATED) {
            feature.accept(Wish.Action.OnNotesUpdated)
        }
    }

    private fun getFormattedDay(day: LocalDate): String {
        val dayOfWeekName = requireContext().getStringArray(R.array.days_of_week).getOrElse(day.dayOfWeek.value - 1) { "" }
        val dayOfMonthName = requireContext().getStringArray(R.array.months).getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $dayOfMonthName"
    }

    private fun getFormattedWeek(weekNumber: Int): String {
        if (weekNumber in 1..16) {
            return requireContext().getString(R.string.schedule_semester_week, weekNumber)
        } else {
            return requireContext().getString(R.string.schedule_weekend_week)
        }
    }
}