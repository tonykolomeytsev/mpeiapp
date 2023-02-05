package kekmech.ru.feature_map.elm

import kekmech.ru.domain_app_settings.AppSettings
import vivid.money.elmslie.core.store.ElmStore

internal class MapFeatureFactory(
    private val actor: MapActor,
    private val appSettings: AppSettings
) {

    fun create() = ElmStore(
        initialState = MapState(appSettings = appSettings),
        reducer = MapReducer(),
        actor = actor
    )
}
