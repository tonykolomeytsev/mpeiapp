package ru.kekmech.common_images.imagepicker.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class OnBottomReachedListener(
    private val gridLayoutManager: GridLayoutManager,
    private val onBottomReachedListener: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5 * gridLayoutManager.spanCount

    init {
        visibleThreshold *= gridLayoutManager.spanCount
    }


}