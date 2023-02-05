package kekmech.ru.feature_map.di

import kekmech.ru.feature_map.elm.MapFeatureFactory
import kekmech.ru.feature_map.launcher.DeeplinkDelegate

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val deeplinkDelegate: DeeplinkDelegate
)
