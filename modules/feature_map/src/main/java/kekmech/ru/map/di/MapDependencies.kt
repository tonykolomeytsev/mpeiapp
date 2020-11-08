package kekmech.ru.map.di

import kekmech.ru.map.launcher.DeeplinkDelegate
import kekmech.ru.map.presentation.MapFeatureFactory

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val deeplinkDelegate: DeeplinkDelegate
)