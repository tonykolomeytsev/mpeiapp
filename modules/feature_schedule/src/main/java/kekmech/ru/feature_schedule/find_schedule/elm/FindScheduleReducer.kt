package kekmech.ru.feature_schedule.find_schedule.elm

import kekmech.ru.domain_schedule.GROUP_NUMBER_PATTERN
import kekmech.ru.domain_schedule.PERSON_NAME_PATTERN
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.News
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.Wish
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result

internal typealias FindScheduleResult = Result<FindScheduleState, FindScheduleEffect, FindScheduleAction>

private const val MESSAGE_BAD_REQUEST = "HTTP 503 Service Unavailable"

internal class FindScheduleReducer :
    StateReducer<FindScheduleEvent, FindScheduleState, FindScheduleEffect, FindScheduleAction> {

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
            command = FindScheduleAction.FindGroup(scheduleName = event.scheduleName)
        )
        is Wish.Action.GroupNumberChanged -> Result(
            state = state.copy(
                isContinueButtonEnabled = event.scheduleName.isValidGroupNumberOrPersonName()
            ),
            command = event.scheduleName.takeIf { it.length >= 2 }
                ?.let(FindScheduleAction::SearchForAutocomplete)
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
            effect = FindScheduleEffect.NavigateNextFragment(state.continueTo, event.scheduleName),
            command = FindScheduleAction.SelectGroup(event.scheduleName)
                .takeIf { state.selectScheduleAfterSuccess }
        )
        is News.SearchResultsLoaded -> Result(
            state = state.copy(searchResults = event.results)
        )
    }

    private fun calculateErrorEffect(throwable: Throwable): FindScheduleEffect = when {
        throwable.message == MESSAGE_BAD_REQUEST -> FindScheduleEffect.ShowError
        else -> FindScheduleEffect.ShowSomethingWentWrongError
    }

    private fun String.isValidGroupNumberOrPersonName() =
        isNotBlank() && (matches(GROUP_NUMBER_PATTERN) || matches(PERSON_NAME_PATTERN))
}