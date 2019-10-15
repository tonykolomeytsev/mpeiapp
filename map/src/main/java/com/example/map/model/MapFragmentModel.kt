package com.example.map.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel

interface MapFragmentModel {
    val buildings: LiveData<List<Building>>
    val hostels: LiveData<List<Hostel>>
    val foods: LiveData<List<Food>>
}