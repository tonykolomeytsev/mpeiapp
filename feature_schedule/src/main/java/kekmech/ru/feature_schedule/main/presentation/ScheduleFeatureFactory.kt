package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseFeature

class ScheduleFeatureFactory {

    fun create(): ScheduleFeature = BaseFeature(
        initialState = ScheduleState(),
        reducer = ScheduleReducer(),
        actor = ScheduleActor()
    ).start()
}