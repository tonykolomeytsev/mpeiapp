package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.common_kotlin.Resource
import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.common_kotlin.toResource
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState as State

internal class DashboardReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.GetSelectedScheduleSuccess -> {
                state { copy(selectedSchedule = event.selectedSchedule) }
            }

            is Internal.GetWeekOfSemesterSuccess -> {
                state { copy(weekOfSemester = event.weekOfSemester.toResource()) }
            }

            is Internal.GetWeekOfSemesterFailure -> {
                state { copy(weekOfSemester = event.throwable.toResource()) }
            }

            is Internal.GetUpcomingEventsSuccess -> {
                state { copy(upcomingEvents = event.upcomingEvents.toResource()) }
            }

            is Internal.GetUpcomingEventsFailure -> {
                state { copy(upcomingEvents = event.throwable.toResource()) }
            }

            is Internal.GetActualNotesSuccess -> {
                state { copy(actualNotes = event.notes.toResource()) }
            }

            is Internal.GetActualNotesFailure -> {
                state { copy(actualNotes = event.throwable.toResource()) }
            }

            is Internal.GetFavoriteSchedulesSuccess -> {
                state { copy(favoriteSchedules = event.favorites.toResource()) }
            }

            is Internal.GetFavoriteSchedulesFailure -> {
                state { copy(favoriteSchedules = event.throwable.toResource()) }
            }

            is Internal.SelectScheduleSuccess -> refreshCommands()
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> refreshCommands()
            is Ui.Action.SwipeToRefresh -> refreshCommands()
            is Ui.Click.ClassesItem -> effects {
                val shownDay =
                    when (val events = state.upcomingEvents.value) {
                        is UpcomingEventsPrediction.ClassesTodayStarted,
                        is UpcomingEventsPrediction.ClassesTodayNotStarted,
                            -> moscowLocalDate()

                        is UpcomingEventsPrediction.ClassesInNDays -> events.date
                        else -> null
                    }
                shownDay?.let { +Effect.NavigateToNotesList(event.classes, it) }
            }

            is Ui.Action.SilentUpdate -> refreshCommands()
            is Ui.Action.SelectFavoriteSchedule -> {
                state {
                    copy(
                        upcomingEvents = Resource.Loading,
                        actualNotes = Resource.Loading,
                    )
                }
                commands {
                    +Command.SelectSchedule(
                        SelectedSchedule(
                            name = event.favoriteSchedule.name,
                            type = event.favoriteSchedule.type,
                        )
                    )
                }
            }
        }
    }

    private fun Result.refreshCommands() {
        commands {
            +Command.GetSelectedSchedule
            +Command.GetWeekOfSemester
            +Command.GetUpcomingEvents
            +Command.GetActualNotes
            +Command.GetFavoriteSchedules
        }
    }
}
