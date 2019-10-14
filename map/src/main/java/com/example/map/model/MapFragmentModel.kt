package com.example.map.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building

interface MapFragmentModel {
    val buildings: LiveData<List<Building>>
}