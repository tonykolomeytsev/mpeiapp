package com.example.map.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.Building

interface MapFragmentView : LifecycleOwner {
    val contentView: ViewGroup

    fun placeContentUnderStatusBar()
    fun setAdapter(mapUIAdapter: RecyclerView.Adapter<*>)

    companion object {
        const val PAGE_BUILDINGS = 0
        const val PAGE_HOSTELS = 1
        const val PAGE_FOODS = 2
    }
}