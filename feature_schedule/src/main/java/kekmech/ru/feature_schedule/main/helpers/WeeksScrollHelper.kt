package kekmech.ru.feature_schedule.main.helpers

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE

class WeeksScrollHelper(
    private val onWeekSelectListener: (Int) -> Unit
) {

    private val pagerSnapHelper = PagerSnapHelper()
    private var adapterPosition: Int = Int.MAX_VALUE / 2
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
        onWeekSelectListener(initialPosition)
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2 + initialPosition)
    }

    fun detach(recyclerView: RecyclerView) {
        pagerSnapHelper.attachToRecyclerView(null)
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private fun calculateNewPosition(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        adapterPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (adapterPosition == -1) return
        val realPosition = adapterPosition - (Int.MAX_VALUE / 2)
        position = realPosition
        onWeekSelectListener(realPosition)
    }

}