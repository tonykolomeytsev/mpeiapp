package kekmech.ru.feature_schedule.screens.find_schedule.elm

import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor,
) {

    fun create(
        continueTo: ScheduleFeatureLauncher.ContinueTo,
        selectGroupAfterSuccess: Boolean,
    ) =
        ElmStoreCompat(
            initialState = FindScheduleState(
                continueTo = continueTo,
                selectScheduleAfterSuccess = selectGroupAfterSuccess,
            ),
            reducer = FindScheduleReducer(),
            actor = findScheduleActor,
            startEvent = FindScheduleEvent.Ui.Init,
        )
}
