package kekmech.ru.feature_map.di

import kekmech.ru.feature_map.launcher.DeeplinkDelegate
import kekmech.ru.feature_map.screens.main.elm.MapFeatureFactory

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val deeplinkDelegate: DeeplinkDelegate
)
