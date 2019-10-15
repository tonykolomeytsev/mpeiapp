package com.example.map.view

import androidx.lifecycle.LifecycleOwner

interface MapFragmentView : LifecycleOwner {
    var onBuildingSelected: (Int) -> Unit

    fun setBuildings(list: List<String>)
    fun setBuildingDescription(description: String)
    fun placeContentUnderStatusBar()
}