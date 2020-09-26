package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish
import java.time.DayOfWeek
import java.time.LocalDate

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
            state = state.copy(notes = event.notes)
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
            actions = listOfNotNull(
                DashboardAction.GetSelectedGroupName,
                DashboardAction.LoadNotes,
                DashboardAction.LoadSchedule(0),
                DashboardAction.LoadSchedule(1)
                    .takeIf { LocalDate.now().dayOfWeek == DayOfWeek.SUNDAY }
            )
        )
        is Wish.Action.OnSwipeRefresh -> Result(
            state = state.copy(
                isLoading = true
            ),
            effects = emptyList(),
            actions = listOfNotNull(
                DashboardAction.GetSelectedGroupName,
                DashboardAction.LoadNotes,
                DashboardAction.LoadSchedule(0),
                DashboardAction.LoadSchedule(1)
                    .takeIf { LocalDate.now().dayOfWeek == DayOfWeek.SUNDAY }
            )
        )
    }
}