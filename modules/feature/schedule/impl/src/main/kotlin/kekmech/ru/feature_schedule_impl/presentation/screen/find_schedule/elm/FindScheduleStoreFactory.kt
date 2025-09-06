package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm

import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher
import money.vivid.elmslie.core.store.ElmStore

internal class FindScheduleStoreFactory(
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
            startEvent = FindScheduleEvent.Ui.Init,
        )
}
