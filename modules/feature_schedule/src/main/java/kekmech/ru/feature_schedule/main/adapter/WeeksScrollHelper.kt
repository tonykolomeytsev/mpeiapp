package kekmech.ru.feature_schedule.main.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE

private const val HALF_INT_MAX_VALUE = Int.MAX_VALUE / 2

internal class WeeksScrollHelper(
    private val onWeekSelectListener: (Int) -> Unit
) {

    private val pagerSnapHelper = PagerSnapHelper()
    private var adapterPosition: Int = HALF_INT_MAX_VALUE
    private var position: Int = 0
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == SCROLL_STATE_IDLE) calculateNewPosition(recyclerView)
        }
    }

    fun attach(recyclerView: RecyclerView, initialPosition: Int = 0) {
        pagerSnapHelper.attachToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.scrollToPosition(HALF_INT_MAX_VALUE + initialPosition)
    }

    fun detach(recyclerView: RecyclerView) {
        pagerSnapHelper.attachToRecyclerView(null)
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private fun calculateNewPosition(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        adapterPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (adapterPosition == -1) return
        val realPosition = adapterPosition - HALF_INT_MAX_VALUE
        position = realPosition
        onWeekSelectListener(realPosition)
    }
}