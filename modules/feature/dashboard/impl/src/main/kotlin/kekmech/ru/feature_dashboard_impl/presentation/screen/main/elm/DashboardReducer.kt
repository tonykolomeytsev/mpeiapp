package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Ui
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState as State

internal class DashboardReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.GetSelectedScheduleSuccess -> {
                // notes belong to the schedule, reload them if another schedule was selected
                if (event.selectedSchedule != state.selectedSchedule) {
                    commands { +Command.GetActualNotes }
                }
                state { copy(selectedSchedule = event.selectedSchedule) }
            }

            is Internal.ScheduleUpdated -> state {
                copy(
                    weekOfSemester = event.weekOfSemester,
                    upcomingEvents = event.upcomingEvents,
                )
            }

            is Internal.RefreshScheduleFinished -> {
                state { copy(isRefreshingSchedule = false) }
                // without a saved schedule the error is already shown instead of the events
                if (event.error != null && state.upcomingEvents.isData) {
                    effects { +Effect.ShowLoadingError }
                }
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

            is Internal.SelectScheduleSuccess -> updateCommands()
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> commands {
                +Command.ObserveSelectedSchedule
                +Command.ObserveSchedule
                +Command.GetActualNotes
                +Command.GetFavoriteSchedules
            }

            is Ui.Action.SwipeToRefresh -> {
                state { copy(isRefreshingSchedule = true) }
                commands { +Command.RefreshSchedule }
                updateCommands()
            }

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

            is Ui.Action.SilentUpdate -> updateCommands()
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

    /** Re-read everything local, the schedule is refreshed only if it's stale */
    private fun Result.updateCommands() {
        commands {
            +Command.ObserveSchedule
            +Command.GetActualNotes
            +Command.GetFavoriteSchedules
        }
    }
}
