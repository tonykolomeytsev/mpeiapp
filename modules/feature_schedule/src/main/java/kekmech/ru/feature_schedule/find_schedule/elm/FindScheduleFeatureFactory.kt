package kekmech.ru.feature_schedule.find_schedule.elm

import vivid.money.elmslie.core.store.ElmStore

internal class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor
) {

    fun create(continueTo: String, selectGroupAfterSuccess: Boolean) = ElmStore(
        initialState = FindScheduleState(
            continueTo = continueTo,
            selectScheduleAfterSuccess = selectGroupAfterSuccess
        ),
        reducer = FindScheduleReducer(),
        actor = findScheduleActor
    ).start()
}