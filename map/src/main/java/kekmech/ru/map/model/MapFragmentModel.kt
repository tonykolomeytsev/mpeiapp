package com.example.map.model

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.Marker
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel

interface MapFragmentModel {
    val buildings: LiveData<List<Building>>
    val hostels: LiveData<List<Hostel>>
    val foods: LiveData<List<Food>>

    var state: Int

    val markers: MutableList<Marker>

    companion object {
        const val PAGE_BUILDINGS = 0
        const val PAGE_HOSTELS = 1
        const val PAGE_FOODS = 2
    }
}