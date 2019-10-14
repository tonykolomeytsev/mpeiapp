package com.example.map.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building
import kekmech.ru.core.usecases.GetBuildingsUseCase
import javax.inject.Inject

class MapFragmentModelImpl @Inject constructor(
    private val getBuildingsUseCase: GetBuildingsUseCase
) : MapFragmentModel {
    override val buildings: LiveData<List<Building>>
        get() = getBuildingsUseCase()
}