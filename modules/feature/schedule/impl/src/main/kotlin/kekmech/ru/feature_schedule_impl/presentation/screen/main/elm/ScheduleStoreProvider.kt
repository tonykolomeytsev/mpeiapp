package kekmech.ru.feature_schedule_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent
import money.vivid.elmslie.core.store.Store
import money.vivid.elmslie.core.store.ElmStore
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect as Effect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent as Event
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState as State

internal typealias ScheduleStore = Store<Event, Effect, State>

internal class ScheduleStoreProvider(
    private val actor: ScheduleActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    private val store by fastLazy {
        ElmStore(
            initialState = State(
                appSettings = appSettingsRepository.getAppSettings()
            ),
            reducer = ScheduleReducer(),
            actor = actor,
            startEvent = ScheduleEvent.Ui.Init,
        )
    }

    fun get(): ScheduleStore = store
}
