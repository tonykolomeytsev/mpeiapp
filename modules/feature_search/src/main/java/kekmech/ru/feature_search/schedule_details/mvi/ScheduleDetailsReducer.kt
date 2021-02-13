package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_schedule.dto.Schedule

internal class ScheduleDetailsReducer : BaseReducer<ScheduleDetailsState, ScheduleDetailsEvent, ScheduleDetailsEffect, ScheduleDetailsAction> {

    override fun reduce(
        event: ScheduleDetailsEvent,
        state: ScheduleDetailsState
    ): Result<ScheduleDetailsState, ScheduleDetailsEffect, ScheduleDetailsAction> = when (event) {
        is ScheduleDetailsEvent.Wish.Init -> Result(
            state = state,
            actions = listOf(
                ScheduleDetailsAction.LoadSchedule(state.searchResult.name, weekOffset = 0),
                ScheduleDetailsAction.LoadSchedule(state.searchResult.name, weekOffset = 1)
            ),
            effects = emptyList()
        )
        is ScheduleDetailsEvent.News.ScheduleLoaded -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeekSchedule = event.schedule.mapToClasses())
            } else {
                state.copy(nextWeekSchedule = event.schedule.mapToClasses())
            }
            Result(newState)
        }
        is ScheduleDetailsEvent.News.LoadScheduleError -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeekSchedule = emptyList())
            } else {
                state.copy(nextWeekSchedule = emptyList())
            }
            Result(newState)
        }
    }

    private fun Schedule.mapToClasses() = weeks
        .flatMap { it.days }
        .flatMap { it.classes }
}