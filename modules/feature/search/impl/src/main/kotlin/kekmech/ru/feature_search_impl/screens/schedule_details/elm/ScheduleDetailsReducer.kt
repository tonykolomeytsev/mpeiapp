package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import java.time.DayOfWeek
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEffect as Effect
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent as Event
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState as State

/**
 * Works with the weeks and days like the reducer of the Schedule tab,
 * so the found schedule is browsed the same way as the selected one
 */
internal class ScheduleDetailsReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.ScheduleUpdated -> state {
                copy(
                    schedule = schedule + (event.weekOffset to event.resource),
                    observeErrors = observeErrors - event.weekOffset,
                )
            }

            is Internal.ObserveScheduleFailure -> state {
                copy(observeErrors = observeErrors + (event.weekOffset to event.throwable))
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
                    isFavoritesUpdating = false,
                    favoriteSchedule = null,
                )
            }

            is Internal.AddToFavoritesSuccess -> state {
                copy(
                    isInFavorites = true,
                    isFavoritesUpdating = false,
                    favoriteSchedule = event.schedule,
                )
            }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> {
                state { showNextWeekIfWeekend() }
                commands {
                    // Both weeks are needed for the classes summary in the header
                    +observeSchedule(weekOffset = 0)
                    +observeSchedule(weekOffset = 1)
                    +Command.GetFavorites
                }
            }

            is Ui.Action.SelectWeek -> selectWeek(event.weekOffset)
            is Ui.Action.PageChanged -> {
                // Swiping past Saturday or Monday leads to the neighbour week
                val isAnotherWeek = event.weekOffset != state.weekOffset
                state {
                    copy(
                        weekOffset = event.weekOffset,
                        selectedDate = selectedDate
                            .plusWeeks(event.weekOffset.toLong() - weekOffset)
                            .plusDays(event.dayOfWeek.toLong() - selectedDate.dayOfWeek.value),
                        isFavoritesFabVisible = true,
                    )
                }
                if (isAnotherWeek) commands { +observeSchedule(event.weekOffset) }
            }

            is Ui.Action.ClassesScrolled -> state { copy(isFavoritesFabVisible = event.dy <= 0) }
            is Ui.Click.Day -> state {
                copy(
                    selectedDate = event.date,
                    isFavoritesFabVisible = true,
                )
            }

            is Ui.Click.Reload -> reload()
            is Ui.Click.Favorites -> {
                // The button stays clickable while the favorites update, so the repeated clicks are ignored
                if (state.isFavoritesUpdating) return
                state { copy(isFavoritesUpdating = true) }
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
        }
    }

    private fun Result.selectWeek(weekOffset: Int) {
        if (state.weekOffset == weekOffset) return
        state {
            copy(
                weekOffset = weekOffset,
                selectedDate = selectedDate.plusWeeks(weekOffset.toLong() - this.weekOffset),
            )
        }
        commands { +observeSchedule(weekOffset) }
    }

    private fun Result.reload() {
        val weekOffset = state.weekOffset
        val key = state.selectedResource?.key
        if (key != null && weekOffset !in state.observeErrors) {
            // the subscription is alive, it will receive the result of the refresh
            commands { +Command.RefreshSchedule(key) }
        } else {
            state { copy(observeErrors = observeErrors - weekOffset) }
            commands { +observeSchedule(weekOffset) }
        }
    }

    private fun Result.observeSchedule(weekOffset: Int): Command =
        Command.ObserveSchedule(
            type = state.scheduleType,
            name = state.searchResult.name,
            weekOffset = weekOffset,
        )

    /**
     * Opens on Monday of the next week on weekends, like the Schedule tab does
     */
    private fun State.showNextWeekIfWeekend(): State =
        when (selectedDate.dayOfWeek) {
            DayOfWeek.SATURDAY -> copy(selectedDate = selectedDate.plusDays(2), weekOffset = weekOffset + 1)
            DayOfWeek.SUNDAY -> copy(selectedDate = selectedDate.plusDays(1), weekOffset = weekOffset + 1)
            else -> this
        }
}
