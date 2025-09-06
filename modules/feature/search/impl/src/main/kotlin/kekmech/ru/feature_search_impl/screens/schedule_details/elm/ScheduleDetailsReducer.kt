package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEffect as Effect
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent as Event
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState as State

internal class ScheduleDetailsReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
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

            is Internal.GetFavoritesSuccess -> {
                val favoriteSchedule =
                    event.schedules.find { it.name == state.searchResult.name }
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
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> commands {
                val type = state.scheduleType
                val name = state.searchResult.name
                +Command.LoadSchedule(type, name, weekOffset = 0)
                +Command.LoadSchedule(type, name, weekOffset = 1)
                +Command.GetFavorites
            }

            is Ui.Click.Day -> state { copy(selectedDayDate = event.date) }
            is Ui.Click.Favorites -> {
                state { copy(isInFavorites = null) }
                val favoriteSchedule = state.favoriteSchedule
                if (favoriteSchedule == null) {
                    val newFavorite =
                        FavoriteSchedule(
                            name = state.searchResult.name,
                            type = state.scheduleType,
                            description = state.searchResult.description,
                            order = 0,
                        )
                    commands { +Command.AddToFavorites(newFavorite) }
                } else {
                    commands { +Command.RemoveFromFavorites(favoriteSchedule) }
                }
            }

            is Ui.Click.SwitchSchedule -> {
                commands {
                    +Command.SwitchSchedule(
                        SelectedSchedule(
                            name = state.searchResult.name,
                            type = state.scheduleType,
                        )
                    )
                }
                effects { +Effect.CloseAndGoToSchedule }
            }
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
