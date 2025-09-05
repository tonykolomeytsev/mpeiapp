package kekmech.ru.feature_schedule.screens.find_schedule.elm

import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import money.vivid.elmslie.core.store.ElmStore

internal class FindScheduleFeatureFactory(
    private val findScheduleActor: FindScheduleActor,
) {

    fun create(
        continueTo: ScheduleFeatureLauncher.ContinueTo,
        selectGroupAfterSuccess: Boolean,
    ) =
        ElmStore(
            initialState = FindScheduleState(
                continueTo = continueTo,
                selectScheduleAfterSuccess = selectGroupAfterSuccess,
            ),
            reducer = FindScheduleReducer(),
            actor = findScheduleActor,
        )
}
