package kekmech.ru.feature_map.screens.main.elm

import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_map.screens.main.elm.MapEvent
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_map.screens.main.elm.MapEffect as Effect
import kekmech.ru.feature_map.screens.main.elm.MapEvent as Event
import kekmech.ru.feature_map.screens.main.elm.MapState as State

internal typealias MapStore = Store<Event, Effect, State>

internal class MapStoreProvider(
    private val actor: MapActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    private val store by fastLazy {
        ElmStoreCompat(
            initialState = State(
                appSettings = appSettingsRepository.getAppSettings().blockingGet()
            ),
            reducer = MapReducer(),
            actor = actor,
            startEvent = MapEvent.Ui.Init,
        )
    }

    fun get(): MapStore = store
}
