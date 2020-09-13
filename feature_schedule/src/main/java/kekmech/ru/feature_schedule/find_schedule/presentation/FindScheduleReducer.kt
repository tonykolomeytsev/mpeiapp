package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleEvent.News
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleEvent.Wish

typealias FindScheduleResult = Result<FindScheduleState, FindScheduleEffect, FindScheduleAction>

private const val MESSAGE_BAD_REQUEST = "HTTP 503 Service Unavailable"

class FindScheduleReducer : BaseReducer<FindScheduleState, FindScheduleEvent, FindScheduleEffect, FindScheduleAction> {

    override fun reduce(
        event: FindScheduleEvent,
        state: FindScheduleState
    ): FindScheduleResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceWish(
        event: Wish,
        state: FindScheduleState
    ): FindScheduleResult = when (event) {
        is Wish.Init -> Result(state = state)
        is Wish.Click.Continue -> Result(
            state = state.copy(isLoading = true),
            action = FindScheduleAction.FindGroup(groupName = event.groupName)
        )
        is Wish.Action.GroupNumberChanged -> Result(
            state = state.copy(
                isContinueButtonEnabled = event.groupName.isValidGroupNumber()
            )
        )
    }

    private fun reduceNews(
        event: News,
        state: FindScheduleState
    ): FindScheduleResult = when (event) {
        is News.GroupLoadingError -> Result(
            state = state.copy(isLoading = false),
            effect = calculateErrorEffect(event.throwable)
        )
        is News.GroupLoadedSuccessfully -> Result(
            state = state.copy(isLoading = false),
            effect = FindScheduleEffect.NavigateNextFragment(state.continueTo),
            action = FindScheduleAction.SelectGroup(event.groupName)
        )
    }

    private fun calculateErrorEffect(throwable: Throwable): FindScheduleEffect = when {
        throwable.message == MESSAGE_BAD_REQUEST -> FindScheduleEffect.ShowError
        else -> FindScheduleEffect.ShowSomethingWentWrongError
    }

    private fun String.isValidGroupNumber() =
        matches("[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()) && isNotBlank()
}