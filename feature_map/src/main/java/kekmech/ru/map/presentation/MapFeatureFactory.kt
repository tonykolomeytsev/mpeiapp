package kekmech.ru.map.presentation

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.map.view.MarkersBitmapFactory

class MapFeatureFactory(
    private val actor: MapActor,
    private val mapMarkersBitmapFactory: MarkersBitmapFactory
) {

    fun create() = BaseFeature(
        initialState = MapState(),
        reducer = MapReducer(mapMarkersBitmapFactory),
        actor = actor
    ).start()
}