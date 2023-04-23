package kekmech.ru.feature_schedule.main.elm

import kekmech.ru.domain_app_settings.AppSettings
import vivid.money.elmslie.core.store.ElmStore

internal class ScheduleFeatureFactory(
    private val actor: ScheduleActor,
    private val appSettings: AppSettings,
) {

    fun create() =
        ElmStore(
            initialState = ScheduleState(appSettings = appSettings),
            reducer = ScheduleReducer(),
            actor = actor,
        )
}
