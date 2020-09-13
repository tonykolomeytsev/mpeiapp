package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.di.ScheduleDependencies
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

    private val infiniteWeekDaysSnapHelper by fastLazy { PagerSnapHelper() }
    private val infiniteScrollHelper by fastLazy { InfiniteScrollHelper() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerDayItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerDayItems.scrollToPosition(Int.MAX_VALUE / 2)
        infiniteWeekDaysSnapHelper.attachToRecyclerView(recyclerDayItems)
        infiniteScrollHelper.attach(recyclerDayItems)
    }

    override fun handleEffect(effect: ScheduleEffect) {

    }

    override fun render(state: ScheduleState) {
        if (recyclerDayItems.adapter == null && state.firstDayOfCurrentWeek != null) {
            recyclerDayItems.adapter = createInfiniteWeekDayAdapter { weekOffset ->
                val firstDayOfWeek = state.firstDayOfCurrentWeek.let { if (weekOffset != 0) it.plusWeeks(weekOffset.toLong()) else it }
                listOf(firstDayOfWeek, *Array(5) { firstDayOfWeek.plusDays(it + 1L) })
            }
        }
    }

    private fun createInfiniteWeekDayAdapter(localDatesGenerator: (Int) -> List<LocalDate>) =
        InfiniteScrollAdapter(localDatesGenerator).apply {
            setOnStartReached {
                println("Left reached $it")
                feature.accept(Wish.Action.WeekDaysStartReached(it)) }
            setOnEndReached {
                println("Right reached $it")
                feature.accept(Wish.Action.WeekDaysEndReached(it)) }
        }
}