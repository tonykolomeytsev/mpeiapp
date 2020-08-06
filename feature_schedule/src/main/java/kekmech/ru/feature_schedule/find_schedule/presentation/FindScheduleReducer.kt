package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias FindScheduleResult = Result<FindScheduleState, FindScheduleEffect, FindScheduleAction>

class FindScheduleReducer() : BaseReducer<FindScheduleState, FindScheduleEvent, FindScheduleEffect, FindScheduleAction> {

    override fun reduce(
        event: FindScheduleEvent,
        state: FindScheduleState
    ): Result<FindScheduleState, FindScheduleEffect, FindScheduleAction> {
        return FindScheduleResult(state = state)
    }
}