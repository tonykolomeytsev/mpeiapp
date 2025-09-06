package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEffect as Effect
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent as Event
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState as State

internal typealias MapStore = Store<Event, Effect, State>

internal class MapStoreFactory(
    private val actor: MapActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    fun create(): MapStore = ElmStore(
        initialState = State(
            appSettings = appSettingsRepository.getAppSettings()
        ),
        reducer = MapReducer(),
        actor = actor,
        startEvent = MapEvent.Ui.Init,
    )
}
