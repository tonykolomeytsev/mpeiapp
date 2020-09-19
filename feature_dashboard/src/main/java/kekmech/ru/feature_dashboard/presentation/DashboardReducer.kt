package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_schedule.dto.ClassesStackType
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish

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
        is News.TodayScheduleLoaded -> Result(
            state = state.copy(
                todayClasses = event.listOfClasses.mapIndexed { index, e ->
                    e.stackType = if (index == 0) null else ClassesStackType.MIDDLE
                    e
                }
            )
        )
        is News.TodayScheduleLoadError -> Result(
            state = state.copy(
                isAfterError = true
            ),
            effect = DashboardEffect.ShowLoadingError
        )
    }

    private fun reduceWish(
        event: Wish,
        state: DashboardState
    ): DashboardResult = when (event) {
        is Wish.Init -> Result(
            state = state,
            action = DashboardAction.LoadTodaySchedule
        )
        is Wish.Action.OnSwipeRefresh -> Result(state)
    }
}