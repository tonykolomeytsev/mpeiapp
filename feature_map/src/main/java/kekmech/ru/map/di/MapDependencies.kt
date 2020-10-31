package kekmech.ru.map.di

import kekmech.ru.map.launcher.SelectedPlaceDelegate
import kekmech.ru.map.presentation.MapFeatureFactory

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val selectedPlaceDelegate: SelectedPlaceDelegate
)