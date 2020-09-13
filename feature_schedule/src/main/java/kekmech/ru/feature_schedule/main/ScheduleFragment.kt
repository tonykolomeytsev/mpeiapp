package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.banner.showBanner
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

    private var infiniteScrollAdapter: InfiniteScrollAdapter? = null
    private val infiniteLayoutManager by fastLazy { LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) }
    private val infiniteWeekDaysSnapHelper by fastLazy { PagerSnapHelper() }
    private val infiniteScrollHelper by fastLazy { InfiniteScrollHelper() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        infiniteLayoutManager.initialPrefetchItemCount = 3
        infiniteLayoutManager.isItemPrefetchEnabled = true
        recyclerDayItems.layoutManager = infiniteLayoutManager
        recyclerDayItems.scrollToPosition(Int.MAX_VALUE / 2)
        recyclerDayItems.setHasFixedSize(true)
        recyclerDayItems.setItemViewCacheSize(10)
        infiniteWeekDaysSnapHelper.attachToRecyclerView(recyclerDayItems)
        infiniteScrollHelper.attach(recyclerDayItems)

//        viewPagerClasses.adapter =
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.SelectDay -> (recyclerDayItems.adapter as InfiniteScrollAdapter).selectDay(effect.localDate)
        is ScheduleEffect.ShowWeekLoadingError -> showBanner(R.string.something_went_wrong_error)
    }

    override fun render(state: ScheduleState) {
        if (recyclerDayItems.adapter == null && state.firstDayOfCurrentWeek != null) {
            initInfiniteRecyclerView(state.firstDayOfCurrentWeek)
        }
        infiniteScrollAdapter?.selectDay(state.selectedDay)
    }

    private fun initInfiniteRecyclerView(firstDayOfCurrentWeek: LocalDate) {
        val localDatesGenerator = { weekOffset: Int ->
            val firstDayOfWeek = firstDayOfCurrentWeek.plusWeeks(weekOffset.toLong())
            listOf(firstDayOfWeek, *Array(5) { firstDayOfWeek.plusDays(it + 1L) })
        }
        infiniteScrollAdapter = createInfiniteWeekDayAdapter(localDatesGenerator) {
            feature.accept(Wish.Click.OnDayClick(it))
        }
        recyclerDayItems.adapter = infiniteScrollAdapter
    }

    private fun createInfiniteWeekDayAdapter(
        localDatesGenerator: (Int) -> List<LocalDate>,
        onDayClickListener: (LocalDate) -> Unit
    ) =
        InfiniteScrollAdapter(localDatesGenerator, onDayClickListener)
}