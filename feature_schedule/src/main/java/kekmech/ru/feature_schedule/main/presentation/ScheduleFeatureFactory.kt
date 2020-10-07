package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_app_settings.AppSettings

internal class ScheduleFeatureFactory(
    private val actor: ScheduleActor,
    private val appSettings: AppSettings,
) {

    fun create(): ScheduleFeature = BaseFeature(
        initialState = ScheduleState(appSettings = appSettings),
        reducer = ScheduleReducer(),
        actor = actor
    ).start()
}