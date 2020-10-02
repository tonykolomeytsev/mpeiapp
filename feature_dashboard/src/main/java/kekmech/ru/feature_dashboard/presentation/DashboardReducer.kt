package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit

typealias DashboardResult = Result<DashboardState, DashboardEffect, DashboardAction>

class DashboardReducer : BaseReducer<DashboardState, DashboardEvent, DashboardEffect, DashboardAction> {

    override fun reduce(
        event: DashboardEvent,
        state: DashboardState
    ): DashboardResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: DashboardState
    ): DashboardResult = when (event) {
        is News.ScheduleLoaded -> Result(
            state = state.copy(
                currentWeekSchedule = event.schedule.takeIf { event.weekOffset == 0 } ?: state.currentWeekSchedule,
                nextWeekSchedule = event.schedule.takeIf { event.weekOffset == 1 } ?: state.nextWeekSchedule,
                isAfterError = false,
                isLoading = false
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
            state = state.copy(selectedGroupName = event.groupName)
        )
        is News.NotesLoaded -> Result(
            state = state.copy(notes = getImportantNotes(event.notes))
        )
        is News.NotesLoadError -> Result(
            state = state,
            effect = DashboardEffect.ShowNotesLoadingError
        )
    }

    private fun reduceWish(
        event: Wish,
        state: DashboardState
    ): DashboardResult = when (event) {
        is Wish.Init -> Result(
            state = state.copy(
                isLoading = true
            ),
            effects = emptyList(), // костыль
            actions = refreshActions()
        )
        is Wish.Action.OnSwipeRefresh -> Result(
            state = state.copy(
                isLoading = true
            ),
            effects = emptyList(),
            actions = refreshActions()
        )
    }

    private fun refreshActions(): List<DashboardAction> {
        return listOfNotNull(
            DashboardAction.GetSelectedGroupName,
            DashboardAction.LoadNotes,
            DashboardAction.LoadSchedule(0),
            DashboardAction.LoadSchedule(1)
                .takeIf { moscowLocalDate().dayOfWeek == DayOfWeek.SUNDAY }
        )
    }

    private fun getImportantNotes(notes: List<Note>): List<Note> =
        notes
            .filter { ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) in 0..7 } // todo make with settings
            .sortedBy { it.dateTime }
            .take(5) // todo make with settings
}