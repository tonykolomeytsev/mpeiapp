package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.News
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish

typealias ScheduleResult = Result<ScheduleState, ScheduleEffect, ScheduleAction>

class ScheduleReducer : BaseReducer<ScheduleState, ScheduleEvent, ScheduleEffect, ScheduleAction> {

    override fun reduce(
        event: ScheduleEvent,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        else -> Result(state = state)
    }

    private fun reduceWish(
        event: Wish,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is Wish.Init -> Result(
            state = state.copy(isLoading = true),
            action = ScheduleAction.ObserveSchedule(state.weekOffset)
        )
    }
}