package kekmech.ru.feature_dashboard.elm

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalDateTime
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Internal
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Ui
import kekmech.ru.feature_dashboard.upcoming_events.getDayWithOffset
import kekmech.ru.feature_dashboard.upcoming_events.getOffsetForDayWithActualEvents
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import java.time.temporal.ChronoUnit
import kekmech.ru.feature_dashboard.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard.elm.DashboardState as State

internal class DashboardReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.LoadScheduleSuccess -> state {
                copy(
                    currentWeekSchedule = event.schedule.takeIf { event.weekOffset == 0 }
                        ?: state.currentWeekSchedule,
                    nextWeekSchedule = event.schedule.takeIf { event.weekOffset == 1 }
                        ?: state.nextWeekSchedule,
                    loadingError = null,
                    isLoading = false,
                    lastUpdateDateTime = moscowLocalDateTime(),
                )
            }
            is Internal.LoadScheduleFailure -> {
                state {
                    copy(
                        loadingError = event.throwable,
                        isLoading = false,
                    )
                }
                effects { +Effect.ShowLoadingError }
            }
            is Internal.GetSelectedGroupNameSuccess -> state {
                copy(selectedScheduleName = event.groupName)
            }
            is Internal.LoadNotesSuccess -> state { copy(notes = getImportantNotes(event.notes)) }
            is Internal.LoadNotesFailure -> effects { +Effect.ShowNotesLoadingError }
            is Internal.LoadFavoriteSchedulesSuccess -> state {
                copy(
                    favoriteSchedules = event.favorites
                        .takeIf { it.isNotEmpty() }
                        ?.map {
                            FavoriteScheduleItem(
                                value = it,
                                isSelected = it.groupNumber == state.selectedScheduleName,
                            )
                        }
                )
            }
            is Internal.LoadSessionSuccess -> state { copy(sessionScheduleItems = event.items) }
            is Internal.SelectGroupSuccess -> refreshCommands()
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> {
                state { copy(isLoading = true) }
                refreshCommands()
            }
            is Ui.Action.SwipeToRefresh -> {
                state { copy(isLoading = true) }
                refreshCommands()
            }
            is Ui.Click.ClassesItem -> effects {
                +state.getActualScheduleDayForView()
                    ?.let { day -> Effect.NavigateToNotesList(event.classes, day.date) }
            }
            is Ui.Action.SilentUpdate -> refreshCommands()
            is Ui.Action.SelectFavoriteSchedule -> {
                val newGroupNumber = event.favoriteSchedule.groupNumber
                state { copy(selectedScheduleName = newGroupNumber) }
                commands { +Command.SelectGroup(newGroupNumber) }
            }
        }

    private fun Result.refreshCommands() {
        commands {
            +Command.GetSelectedGroupName
            +Command.LoadNotes
            +Command.LoadSchedule(0)
            +Command.LoadSchedule(1)
            +Command.LoadFavoriteSchedules
            +Command.LoadSession
        }
    }

    @Suppress("MagicNumber")
    private fun getImportantNotes(notes: List<Note>): List<Note> =
        notes
            .filter { // todo make with settings
                ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) in 0..7
            }
            .sortedBy { it.dateTime }
            .take(5) // todo make with settings

    private fun State.getActualScheduleDayForView(): Day? {
        val nowDate = lastUpdateDateTime.toLocalDate()
        val nowTime = lastUpdateDateTime.toLocalTime()
        return getDayWithOffset(nowDate, getOffsetForDayWithActualEvents(nowDate, nowTime))
    }

}
