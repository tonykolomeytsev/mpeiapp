package com.example.map.view

import androidx.lifecycle.LifecycleOwner

interface MapFragmentView : LifecycleOwner {
    fun setBuildings(list: List<String>)
}