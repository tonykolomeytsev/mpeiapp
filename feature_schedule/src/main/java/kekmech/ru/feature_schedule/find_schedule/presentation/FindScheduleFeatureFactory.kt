package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseFeature

class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor
) {

    fun create(continueTo: String): FindScheduleFeature = BaseFeature(
        initialState = FindScheduleState(continueTo = continueTo),
        reducer = FindScheduleReducer(),
        actor = findScheduleActor
    ).start()
}