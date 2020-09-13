package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_schedule.ScheduleRepository

class ScheduleFeatureFactory(
    private val scheduleRepository: ScheduleRepository
) {

    fun create(): ScheduleFeature = BaseFeature(
        initialState = ScheduleState(),
        reducer = ScheduleReducer(),
        actor = ScheduleActor(scheduleRepository)
    ).start()
}