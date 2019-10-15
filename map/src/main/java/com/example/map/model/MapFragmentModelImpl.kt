package com.example.map.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.usecases.GetBuildingsUseCase
import kekmech.ru.core.usecases.GetFoodsUseCase
import kekmech.ru.core.usecases.GetHostelsUseCase
import javax.inject.Inject

class MapFragmentModelImpl @Inject constructor(
    private val getBuildingsUseCase: GetBuildingsUseCase,
    private val getHostelsUseCase: GetHostelsUseCase,
    private val getFoodsUseCase: GetFoodsUseCase
) : MapFragmentModel {
    override val buildings: LiveData<List<Building>>
        get() = getBuildingsUseCase()
    override val hostels: LiveData<List<Hostel>>
        get() = getHostelsUseCase()
    override val foods: LiveData<List<Food>>
        get() = getFoodsUseCase()
}