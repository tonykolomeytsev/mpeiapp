package kekmech.ru.feature_schedule.screens.main.elm

import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEvent
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEffect as Effect
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEvent as Event
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleState as State

internal class ScheduleFeatureFactory(
    private val actor: ScheduleActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    fun create(): Store<Event, Effect, State> {
        val preheatAppSettings = appSettingsRepository.getAppSettings().blockingGet()
        return ElmStoreCompat(
            initialState = State(appSettings = preheatAppSettings),
            reducer = ScheduleReducer(),
            actor = actor,
            startEvent = ScheduleEvent.Ui.Init,
        )
    }
}
