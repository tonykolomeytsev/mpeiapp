package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.banner.showBanner
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

    override val initEvent = Wish.Init

    override fun createFeature() = inject<ScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()

    private val viewPagerAdapter by fastLazy { createViewPagerAdapter() }
    private val weeksScrollAdapter by fastLazy { createWeekScrollAdapter() }
    private val weeksScrollHelper by fastLazy { createWeekDaysHelper() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        weeksScrollHelper.attach(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = weeksScrollAdapter
        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                feature.accept(Wish.Action.OnPageChanged(position))
            }
        })
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.ShowLoadingError -> showBanner(R.string.something_went_wrong_error)
    }

    override fun render(state: ScheduleState) {
        shimmerViewContainer.isVisible = state.isLoading
        appBarLayoutGroup.isVisible = !state.isLoading
        if (state.isLoading) shimmerViewContainer.startShimmer() else shimmerViewContainer.stopShimmer()

        textViewHeader.text = getFormattedDay(state.selectedDay.date)
        textViewDescription.text = state.weekOfSemester?.let { getFormattedWeek(it) }.orEmpty()

        weeksScrollAdapter.update(state.weekItems)
        viewPagerAdapter.update(ScheduleClassesListConverter.map(state))

        val selectedItem = state.selectedDay.dayOfWeek - 1
        if (selectedItem != viewPager.currentItem) viewPager.post {
            if (selectedItem in 0..5) viewPager.setCurrentItem(selectedItem, true)
        }
    }

    private fun createViewPagerAdapter() = BaseAdapter(
        WorkingDayAdapterItem(DAY_OF_WEEK_MONDAY),
        WorkingDayAdapterItem(DAY_OF_WEEK_TUESDAY),
        WorkingDayAdapterItem(DAY_OF_WEEK_WEDNESDAY),
        WorkingDayAdapterItem(DAY_OF_WEEK_THURSDAY),
        WorkingDayAdapterItem(DAY_OF_WEEK_FRIDAY),
        WorkingDayAdapterItem(DAY_OF_WEEK_SATURDAY),
        ClassesShimmerAdapterItem()
    )

    private fun createWeekDaysHelper() = WeeksScrollHelper(
        onWeekSelectListener = { feature.accept(Wish.Action.SelectWeek(it)) }
    )

    private fun createWeekScrollAdapter() = WeeksScrollAdapter(
        WeekAdapterItem(
            onDayClickListener = { feature.accept(Wish.Click.OnDayClick(it)) }
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