package kekmech.ru.map.di

import kekmech.ru.map.launcher.DeeplinkDelegate
import kekmech.ru.map.elm.MapFeatureFactory

internal data class MapDependencies(
    val mapFeatureFactory: MapFeatureFactory,
    val deeplinkDelegate: DeeplinkDelegate
)