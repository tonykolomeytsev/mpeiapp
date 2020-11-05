package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseFeature

internal class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor
) {

    fun create(continueTo: String, selectGroupAfterSuccess: Boolean): FindScheduleFeature = BaseFeature(
        initialState = FindScheduleState(
            continueTo = continueTo,
            selectGroupAfterSuccess = selectGroupAfterSuccess
        ),
        reducer = FindScheduleReducer(),
        actor = findScheduleActor
    ).start()
}