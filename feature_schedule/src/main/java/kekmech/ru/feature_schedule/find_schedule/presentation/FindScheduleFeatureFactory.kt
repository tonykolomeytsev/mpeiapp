package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseFeature

class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor
) {

    fun create(): FindScheduleFeature = BaseFeature(
        initialState = FindScheduleState(),
        reducer = FindScheduleReducer(),
        actor = findScheduleActor
    ).start()
}