package kekmech.ru.feature_schedule.main.elm

import kekmech.ru.domain_app_settings.AppSettingsRepository
import vivid.money.elmslie.core.store.ElmStore
import kekmech.ru.feature_schedule.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule.main.elm.ScheduleEffect as Effect
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent as Event
import kekmech.ru.feature_schedule.main.elm.ScheduleState as State

internal class ScheduleFeatureFactory(
    private val actor: ScheduleActor,
    private val appSettingsRepository: AppSettingsRepository,
) {

    fun create(): ElmStore<Event, State, Effect, Command> {
        val preheatAppSettings = appSettingsRepository.getAppSettings().blockingGet()
        return ElmStore(
            initialState = State(appSettings = preheatAppSettings),
            reducer = ScheduleReducer(),
            actor = actor,
        )
    }
}
