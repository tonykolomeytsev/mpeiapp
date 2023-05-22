package kekmech.ru.feature_map.screens.main.elm

import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_map.screens.main.elm.MapEvent
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_map.screens.main.elm.MapCommand as Command
import kekmech.ru.feature_map.screens.main.elm.MapEffect as Effect
import kekmech.ru.feature_map.screens.main.elm.MapEvent as Event
import kekmech.ru.feature_map.screens.main.elm.MapState as State

internal class MapFeatureFactory(
    private val actor: MapActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    fun create(): Store<Event, Effect, State> {
        val preheatAppSettings = appSettingsRepository.getAppSettings().blockingGet()
        return ElmStoreCompat(
            initialState = State(appSettings = preheatAppSettings),
            reducer = MapReducer(),
            actor = actor,
            startEvent = MapEvent.Ui.Init,
        )
    }
}
