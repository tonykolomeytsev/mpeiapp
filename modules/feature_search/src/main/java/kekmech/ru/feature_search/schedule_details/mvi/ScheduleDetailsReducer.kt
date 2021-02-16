package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_schedule.dto.Day
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
                ScheduleDetailsAction.LoadSchedule(state.searchResult.name, weekOffset = 1),
                ScheduleDetailsAction.LoadFavorites
            ),
            effects = emptyList()
        )
        is ScheduleDetailsEvent.Wish.Click.Favorites -> Result(state)
        is ScheduleDetailsEvent.Wish.Click.Day -> Result(state.copy(selectedDayDate = event.date))
        is ScheduleDetailsEvent.News.ScheduleLoaded -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeek = event.schedule.mapToDays())
            } else {
                state.copy(nextWeek = event.schedule.mapToDays())
            }
            Result(newState)
        }
        is ScheduleDetailsEvent.News.LoadScheduleError -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeek = emptyList())
            } else {
                state.copy(nextWeek = emptyList())
            }
            Result(newState)
        }
        is ScheduleDetailsEvent.News.FavoritesLoaded -> Result(
            state.copy(isInFavorites = event.names.any { it.groupNumber == state.searchResult.name })
        )
    }

    private fun Schedule.mapToDays(): List<Day> {
        val week = weeks.firstOrNull() ?: return emptyList()
        val days = weeks.flatMap { it.days }
        return (1 .. 7)
            .map { dayOfWeek ->
                days.find { it.dayOfWeek == dayOfWeek }
                    ?: Day(dayOfWeek, week.firstDayOfWeek.plusDays(dayOfWeek - 1L))
            }
    }
}