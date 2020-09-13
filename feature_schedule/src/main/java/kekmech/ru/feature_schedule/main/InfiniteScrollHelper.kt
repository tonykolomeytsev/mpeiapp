package kekmech.ru.feature_schedule.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class InfiniteScrollHelper {

    private var onStartReached: () -> Unit = {}
    private var onEndReached: () -> Unit = {}
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) calculateReachedEdges(recyclerView)
        }
    }

    fun attach(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(scrollListener)
        calculateReachedEdges(recyclerView)
    }

    fun detach(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private fun calculateReachedEdges(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
        val adapter = recyclerView.adapter
        if (layoutManager != null && adapter != null) {
            if (layoutManager.findFirstVisibleItemPosition() == 0) onStartReached()
            if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) onEndReached()
        } else {
            Timber.d("RecyclerView adapter or linearLayout is null")
        }
    }

    fun setOnStartReached(listener: () -> Unit) { onStartReached = listener }

    fun setOnEndReached(listener: () -> Unit) { onEndReached = listener }
}