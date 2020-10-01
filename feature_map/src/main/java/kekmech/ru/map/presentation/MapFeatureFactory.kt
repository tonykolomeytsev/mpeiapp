package kekmech.ru.map.presentation

import kekmech.ru.common_mvi.BaseFeature

class MapFeatureFactory(
    private val actor: MapActor
) {

    fun create() = BaseFeature(
        initialState = MapState(),
        reducer = MapReducer(),
        actor = actor
    ).start()
}