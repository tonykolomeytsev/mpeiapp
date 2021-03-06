package kekmech.ru.feature_dashboard.elm

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalDateTime
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.feature_dashboard.elm.DashboardEvent.News
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.upcoming_events.getDayWithOffset
import kekmech.ru.feature_dashboard.upcoming_events.getOffsetForDayWithActualEvents
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer
import java.time.temporal.ChronoUnit

class DashboardReducer : StateReducer<DashboardEvent, DashboardState, DashboardEffect, DashboardAction> {

    override fun reduce(
        event: DashboardEvent,
        state: DashboardState
    ): Result<DashboardState, DashboardEffect, DashboardAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: DashboardState
    ): Result<DashboardState, DashboardEffect, DashboardAction> = when (event) {
        is News.ScheduleLoaded -> Result(
            state = state.copy(
                currentWeekSchedule = event.schedule.takeIf { event.weekOffset == 0 } ?: state.currentWeekSchedule,
                nextWeekSchedule = event.schedule.takeIf { event.weekOffset == 1 } ?: state.nextWeekSchedule,
                isAfterError = false,
                isLoading = false,
                lastUpdateDateTime = moscowLocalDateTime()
            )
        )
        is News.ScheduleLoadError -> Result(
            state = state.copy(
                isAfterError = true,
                isLoading = false
            ),
            effect = DashboardEffect.ShowLoadingError
        )
        is News.SelectedGroupNameLoaded -> Result(
            state = state.copy(selectedScheduleName = event.groupName)
        )
        is News.NotesLoaded -> Result(
            state = state.copy(notes = getImportantNotes(event.notes))
        )
        is News.NotesLoadError -> Result(
            state = state,
            effect = DashboardEffect.ShowNotesLoadingError
        )
        is News.FavoriteSchedulesLoaded -> Result(
            state = state.copy(
                favoriteSchedules = event.favorites
                    .takeIf { it.isNotEmpty() }
                    ?.map { FavoriteScheduleItem(it, it.groupNumber == state.selectedScheduleName) }
            )
        )
        is News.SessionLoaded -> Result(state = state.copy(sessionScheduleItems = event.items))
        is News.FavoriteGroupSelected -> Result(
            state = state,
            commands = refreshActions()
        )
    }

    private fun reduceWish(
        event: Wish,
        state: DashboardState
    ): Result<DashboardState, DashboardEffect, DashboardAction> = when (event) {
        is Wish.Init -> Result(
            state = state.copy(isLoading = true),
            commands = refreshActions()
        )
        is Wish.Action.OnSwipeRefresh -> Result(
            state = state.copy(
                isLoading = true
            ),
            commands = refreshActions()
        )
        is Wish.Click.OnClasses -> Result(
            state = state,
            effect = state.getActualScheduleDayForView()
                ?.let { day -> DashboardEffect.NavigateToNotesList(event.classes, day.date) }
        )
        is Wish.Action.SilentUpdate -> Result(
            state = state,
            commands = refreshActions()
        )
        is Wish.Action.SelectFavoriteSchedule -> {
            val newGroupNumber = event.favoriteSchedule.groupNumber
            Result(
                state = state.copy(selectedScheduleName = newGroupNumber),
                commands = listOf(DashboardAction.SelectGroup(newGroupNumber))
            )
        }
    }

    private fun refreshActions(): List<DashboardAction> {
        return listOfNotNull(
            DashboardAction.GetSelectedGroupName,
            DashboardAction.LoadNotes,
            DashboardAction.LoadSchedule(0),
            DashboardAction.LoadSchedule(1),
            DashboardAction.LoadFavoriteSchedules,
            DashboardAction.LoadSession
        )
    }

    @Suppress("MagicNumber")
    private fun getImportantNotes(notes: List<Note>): List<Note> =
        notes
            .filter { // todo make with settings
                ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) in 0..7
            }
            .sortedBy { it.dateTime }
            .take(5) // todo make with settings

    private fun DashboardState.getActualScheduleDayForView(): Day? {
        val nowDate = lastUpdateDateTime.toLocalDate()
        val nowTime = lastUpdateDateTime.toLocalTime()
        return getDayWithOffset(nowDate, getOffsetForDayWithActualEvents(nowDate, nowTime))
    }

}