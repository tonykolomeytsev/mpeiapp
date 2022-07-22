package kekmech.ru.map.di

import kekmech.ru.map.elm.MapFeatureFactory
import kekmech.ru.map.launcher.DeeplinkDelegate

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val deeplinkDelegate: DeeplinkDelegate
)
