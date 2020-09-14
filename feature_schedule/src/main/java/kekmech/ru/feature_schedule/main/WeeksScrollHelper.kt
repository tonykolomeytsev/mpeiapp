package kekmech.ru.feature_schedule.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_schedule.main.adapter.WeeksScrollAdapter
import kekmech.ru.feature_schedule.main.item.DayItem
import java.time.LocalDate
import java.util.*

class WeeksScrollHelper(
    onDayClickListener: (DayItem) -> Unit,
    onWeekSelectListener: (Int) -> Unit
) {
    val onDayClickListeners = mutableListOf(onDayClickListener)
    val onWeekSelectListeners = mutableListOf(onWeekSelectListener)
    var currentWeekMonday: LocalDate = LocalDate.now()
    private val localDatesGenerator by fastLazy { LocalDatesGenerator(currentWeekMonday) }
    private val pagerSnapHelper = PagerSnapHelper()
    private var position: Int = 0
    private val adapter by fastLazy { WeeksScrollAdapter(localDatesGenerator) }
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) calculateNewPosition(recyclerView)
        }
    }

    fun attach(recyclerView: RecyclerView) {
        pagerSnapHelper.attachToRecyclerView(recyclerView)
        adapter.setOnDayClickListener { dayItem ->
            onDayClickListeners.forEach { it(dayItem) }
        }
        recyclerView.apply {
            addOnScrollListener(scrollListener)
            if (layoutManager == null) layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            if (adapter == null) adapter = this@WeeksScrollHelper.adapter
            setHasFixedSize(true)
            setItemViewCacheSize(10)
        }
        onWeekSelectListeners.forEach { it(0) }
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
    }

    fun detach(recyclerView: RecyclerView) {
        pagerSnapHelper.attachToRecyclerView(null)
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private fun calculateNewPosition(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val adapterPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val realPosition = -((Int.MAX_VALUE / 2) - adapterPosition)
        position = realPosition
        onWeekSelectListeners.forEach { it(realPosition) }
    }

    class LocalDatesGenerator(
        private val currentWeekMonday: LocalDate
    ) : (Int) -> List<LocalDate> {
        private val weakHashMap = WeakHashMap<Int, List<LocalDate>>()

        override fun invoke(p1: Int): List<LocalDate> {
            val selectedWeek = currentWeekMonday.plusWeeks(p1.toLong())
            return weakHashMap.getOrPut(p1) { Array(6) { selectedWeek.plusDays(it.toLong()) }.asList() }
        }
    }
}