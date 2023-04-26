package kekmech.ru.feature_map.elm

import kekmech.ru.domain_app_settings.AppSettingsRepository
import vivid.money.elmslie.core.store.ElmStore
import kekmech.ru.feature_map.elm.MapCommand as Command
import kekmech.ru.feature_map.elm.MapEffect as Effect
import kekmech.ru.feature_map.elm.MapEvent as Event
import kekmech.ru.feature_map.elm.MapState as State

internal class MapFeatureFactory(
    private val actor: MapActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    fun create(): ElmStore<Event, State, Effect, Command> {
        val preheatAppSettings = appSettingsRepository.getAppSettings().blockingGet()
        return ElmStore(
            initialState = State(appSettings = preheatAppSettings),
            reducer = MapReducer(),
            actor = actor
        )
    }
}
