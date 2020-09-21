package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_schedule.ScheduleRepository

class ScheduleFeatureFactory(
    private val scheduleRepository: ScheduleRepository,
    private val appSettings: AppSettings
) {

    fun create(): ScheduleFeature = BaseFeature(
        initialState = ScheduleState(appSettings = appSettings),
        reducer = ScheduleReducer(),
        actor = ScheduleActor(scheduleRepository)
    ).start()
}