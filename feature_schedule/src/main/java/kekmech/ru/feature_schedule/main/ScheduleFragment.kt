package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule.main.helpers.WeeksScrollHelper
import kekmech.ru.feature_schedule.main.item.*
import kekmech.ru.feature_schedule.main.presentation.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.koin.android.ext.android.inject
import java.time.LocalDate

class ScheduleFragment : BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState, ScheduleFeature>() {

    init { retainInstance = true }

    override val initEvent = Wish.Init

    override fun createFeature() = inject<ScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()

    private val viewPagerAdapter by fastLazy { createViewPagerAdapter() }
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }

    private val analytics: ScheduleAnalytics by inject()

    // for viewPager sliding debounce
    private var viewPagerPositionToBeSelected: Int? = null
    private var weekOffsetToBeSelected: Int = 0

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        weeksScrollHelper.attach(recyclerView, weekOffsetToBeSelected)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        analytics.sendScreenShown()
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.NavigateToNoteList -> {
            dependencies.notesFeatureLauncher.launchNoteList(effect.classes, effect.date)
        }
    }

    override fun render(state: ScheduleState) {
        weekOffsetToBeSelected = state.weekOffset
        shimmerViewContainer.isVisible = state.isLoading
        appBarLayoutGroup.isVisible = !state.isLoading
        if (state.isLoading) shimmerViewContainer.startShimmer() else shimmerViewContainer.stopShimmer()

        textViewHeader.text = getFormattedDay(state.selectedDay.date)
        textViewDescription.text = state.weekOfSemester?.let { getFormattedWeek(it) }.orEmpty()

        weeksScrollAdapter.update(state.weekItems)
        weeksScrollAdapter.selectDay(state.selectedDay)
        viewPagerAdapter.update(ScheduleClassesListConverter.map(state))

        if (viewPagerPositionToBeSelected != state.selectedDay.dayOfWeek) {
            val smoothScroll = viewPagerPositionToBeSelected != null
            viewPagerPositionToBeSelected = state.selectedDay.dayOfWeek
            val selectedItem = state.selectedDay.dayOfWeek - 1
            if (selectedItem != viewPager.currentItem) viewPager.post {
                if (selectedItem in 0..5) viewPager.setCurrentItem(selectedItem, smoothScroll)
            }
        }
    }

    private fun createViewPagerAdapter() = BaseAdapter(
        WorkingDayAdapterItem(DAY_OF_WEEK_MONDAY, ::onClassesClick),
        WorkingDayAdapterItem(DAY_OF_WEEK_TUESDAY, ::onClassesClick),
        WorkingDayAdapterItem(DAY_OF_WEEK_WEDNESDAY, ::onClassesClick),
        WorkingDayAdapterItem(DAY_OF_WEEK_THURSDAY, ::onClassesClick),
        WorkingDayAdapterItem(DAY_OF_WEEK_FRIDAY, ::onClassesClick),
        WorkingDayAdapterItem(DAY_OF_WEEK_SATURDAY, ::onClassesClick),
        ClassesShimmerAdapterItem()
    )

    private fun onClassesClick(classes: Classes) {
        analytics.sendClick("Classes")
        feature.accept(Wish.Click.OnClassesClick(classes))
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