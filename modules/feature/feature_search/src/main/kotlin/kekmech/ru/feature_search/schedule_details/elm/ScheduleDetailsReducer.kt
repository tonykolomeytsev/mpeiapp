package kekmech.ru.feature_search.schedule_details.elm

import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Ui
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEffect as Effect
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent as Event
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsState as State

internal class ScheduleDetailsReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.LoadScheduleSuccess -> state {
                val days = event.schedule.mapToDays()
                if (event.weekOffset == 0) {
                    copy(thisWeek = days)
                } else {
                    copy(nextWeek = days)
                }
            }
            is Internal.LoadScheduleError -> state {
                if (event.weekOffset == 0) {
                    copy(thisWeek = emptyList())
                } else {
                    copy(nextWeek = emptyList())
                }
            }
            is Internal.LoadFavoritesSuccess -> {
                val favoriteSchedule =
                    event.schedules.find { it.groupNumber == state.searchResult.name }
                state {
                    copy(
                        isInFavorites = favoriteSchedule != null,
                        favoriteSchedule = favoriteSchedule,
                    )
                }
            }
            is Internal.RemoveFromFavoritesSuccess -> state {
                copy(
                    isInFavorites = false,
                    favoriteSchedule = null,
                )
            }
            is Internal.AddToFavoritesSuccess -> state {
                copy(
                    isInFavorites = true,
                    favoriteSchedule = event.schedule,
                )
            }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> commands {
                +Command.LoadSchedule(state.searchResult.name, weekOffset = 0)
                +Command.LoadSchedule(state.searchResult.name, weekOffset = 1)
                +Command.LoadFavorites
            }
            is Ui.Click.Day -> state { copy(selectedDayDate = event.date) }
            is Ui.Click.Favorites -> {
                state { copy(isInFavorites = null) }
                val favoriteSchedule = state.favoriteSchedule
                if (favoriteSchedule == null) {
                    val newFavorite =
                        FavoriteSchedule(
                            groupNumber = state.searchResult.name,
                            description = state.searchResult.description,
                            order = 0,
                        )
                    commands { +Command.AddToFavorites(newFavorite) }
                } else {
                    commands { +Command.RemoveFromFavorites(favoriteSchedule) }
                }
            }
            is Ui.Click.SwitchSchedule -> {
                commands { +Command.SwitchSchedule(state.searchResult.name) }
                effects { +Effect.CloseAndGoToSchedule }
            }
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
