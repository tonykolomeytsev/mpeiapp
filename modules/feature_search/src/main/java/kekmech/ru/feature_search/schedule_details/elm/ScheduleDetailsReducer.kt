package kekmech.ru.feature_search.schedule_details.elm

import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.News
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

internal class ScheduleDetailsReducer :
    StateReducer<ScheduleDetailsEvent, ScheduleDetailsState,
            ScheduleDetailsEffect, ScheduleDetailsAction> {

    override fun reduce(
        event: ScheduleDetailsEvent,
        state: ScheduleDetailsState
    ): Result<ScheduleDetailsState, ScheduleDetailsEffect, ScheduleDetailsAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: ScheduleDetailsState
    ): Result<ScheduleDetailsState, ScheduleDetailsEffect, ScheduleDetailsAction> = when(event) {
        is News.ScheduleLoaded -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeek = event.schedule.mapToDays())
            } else {
                state.copy(nextWeek = event.schedule.mapToDays())
            }
            Result(newState)
        }
        is News.LoadScheduleError -> {
            val newState = if (event.weekOffset == 0) {
                state.copy(thisWeek = emptyList())
            } else {
                state.copy(nextWeek = emptyList())
            }
            Result(newState)
        }
        is News.FavoritesLoaded -> {
            val favoriteSchedule = event.schedules.find { it.groupNumber == state.searchResult.name }
            Result(
                state.copy(
                    isInFavorites = favoriteSchedule != null,
                    favoriteSchedule = favoriteSchedule
                )
            )
        }
        is News.FavoriteRemoved -> Result(
            state.copy(isInFavorites = false, favoriteSchedule = null)
        )
        is News.FavoriteAdded -> Result(
            state.copy(isInFavorites = true, favoriteSchedule = event.schedule)
        )
    }

    private fun reduceWish(
        event: Wish,
        state: ScheduleDetailsState
    ): Result<ScheduleDetailsState, ScheduleDetailsEffect, ScheduleDetailsAction> = when(event) {
        is Wish.Init -> Result(
            state = state,
            commands = listOf(
                ScheduleDetailsAction.LoadSchedule(state.searchResult.name, weekOffset = 0),
                ScheduleDetailsAction.LoadSchedule(state.searchResult.name, weekOffset = 1),
                ScheduleDetailsAction.LoadFavorites
            )
        )
        is Wish.Click.Day -> Result(state.copy(selectedDayDate = event.date))
        is Wish.Click.Favorites -> {
            if (state.favoriteSchedule == null) {
                val newFavorite = FavoriteSchedule(
                    groupNumber = state.searchResult.name,
                    description = state.searchResult.description,
                    order = 0
                )
                Result(
                    state = state.copy(isInFavorites = null),
                    command = ScheduleDetailsAction.AddToFavorites(newFavorite)
                )
            } else {
                Result(
                    state = state.copy(isInFavorites = null),
                    command = ScheduleDetailsAction.RemoveFromFavorites(state.favoriteSchedule)
                )
            }
        }
        is Wish.Click.SwitchSchedule -> Result(
            state = state,
            command = ScheduleDetailsAction.SwitchSchedule(state.searchResult.name),
            effect = ScheduleDetailsEffect.CloseAndGoToSchedule
        )
    }

    /**
     * If you modify this function, please, also modify same function in ScheduleDetailsReducerTest.Companion
     */
    private fun Schedule.mapToDays(): List<Day> {
        val week = weeks.firstOrNull() ?: return emptyList()
        val days = weeks.flatMap { it.days }
        return (1..DAYS_IN_WEEK)
            .map { dayOfWeek ->
                days.find { it.dayOfWeek == dayOfWeek }
                    ?: Day(dayOfWeek, week.firstDayOfWeek.plusDays(dayOfWeek - 1L))
            }
    }

    private companion object {
        private const val DAYS_IN_WEEK = 7
    }
}