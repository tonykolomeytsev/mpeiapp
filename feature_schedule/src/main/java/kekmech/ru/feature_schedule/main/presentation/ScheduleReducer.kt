package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.News
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import java.time.LocalDate

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
        is News.ScheduleWeekLoadSuccess -> {
            state.schedule[event.weekOffset] = event.schedule
            if (state.isFirstLoading) {
                val firstDayOfWeek = getFirstDayOfWeek(event.schedule)
                Result(
                    state = state.copy(
                        currentWeekMonday = firstDayOfWeek,
                        selectedDay = LocalDate.now(),
                        isFirstLoading = false,
                        isLoading = false
                    )
                )
            } else {
                Result(
                    state = state.copy(
                        isLoading = false,
                        isFirstLoading = false
                    )
                )
            }
        }
        is News.ScheduleWeekLoadError -> Result(
            state = state.copy(isLoading = false),
            effect = ScheduleEffect.ShowWeekLoadingError(event.throwable)
        )
    }

    private fun reduceWish(
        event: Wish,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is Wish.Init -> Result(
            state = state.copy(isLoading = true),
            action = ScheduleAction.LoadSchedule(state.weekOffset)
        )
        is Wish.Action.SelectWeek -> Result(
            state = state.copy(weekOffset = event.weekOffset),
            action = ScheduleAction.LoadSchedule(event.weekOffset)
        )
        is Wish.Click.OnDayClick -> Result(
            state = state.copy(selectedDay = event.localDate)
        )
    }

    private fun getFirstDayOfWeek(schedule: Schedule) = schedule.weeks.first().firstDayOfWeek
}