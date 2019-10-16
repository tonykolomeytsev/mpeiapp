package com.example.map.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import kekmech.ru.core.dto.Building

interface MapFragmentView : LifecycleOwner {
    val contentView: ViewGroup
    var onBuildingSelected: (Int) -> Unit

    fun selectBuilding(index: Int)
    fun setBuildings(list: List<Building>)
    fun setBuildingDescription(description: String)
    fun placeContentUnderStatusBar()
}