package com.example.map.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.Building

interface MapFragmentView : LifecycleOwner {
    val contentView: ViewGroup
    var onChangeStateListener: (Int) -> Unit

    fun placeContentUnderStatusBar()
    fun setAdapter(mapUIAdapter: RecyclerView.Adapter<*>)
    fun setState(state: Int)
}