package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEffect as Effect
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent as Event
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState as State

internal typealias MapStore = Store<Event, Effect, State>

internal class MapStoreProvider(
    private val actor: MapActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    private val store by fastLazy {
        ElmStoreCompat(
            initialState = State(
                appSettings = appSettingsRepository.getAppSettings()
            ),
            reducer = MapReducer(),
            actor = actor,
            startEvent = MapEvent.Ui.Init,
        )
    }

    fun get(): MapStore = store
}
