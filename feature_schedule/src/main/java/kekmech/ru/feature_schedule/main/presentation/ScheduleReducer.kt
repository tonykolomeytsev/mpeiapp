package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

class ScheduleReducer : BaseReducer<ScheduleState, ScheduleEvent, ScheduleEffect, ScheduleAction> {

    override fun reduce(
        event: ScheduleEvent,
        state: ScheduleState
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> {
        TODO("Not yet implemented")
    }
}