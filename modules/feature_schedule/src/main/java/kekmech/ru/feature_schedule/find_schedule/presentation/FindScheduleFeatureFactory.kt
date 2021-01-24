package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseFeature

internal class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor
) {

    fun create(continueTo: String, selectGroupAfterSuccess: Boolean): FindScheduleFeature = BaseFeature(
        initialState = FindScheduleState(
            continueTo = continueTo,
            selectScheduleAfterSuccess = selectGroupAfterSuccess
        ),
        reducer = FindScheduleReducer(),
        actor = findScheduleActor
    ).start()
}