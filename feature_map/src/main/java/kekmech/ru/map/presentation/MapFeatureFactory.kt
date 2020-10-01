package kekmech.ru.map.presentation

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_app_settings.AppSettings

class MapFeatureFactory(
    private val actor: MapActor,
    private val appSettings: AppSettings
) {

    fun create() = BaseFeature(
        initialState = MapState(appSettings = appSettings),
        reducer = MapReducer(),
        actor = actor
    ).start()
}