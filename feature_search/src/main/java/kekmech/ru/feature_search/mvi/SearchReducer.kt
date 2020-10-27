package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_search.mvi.SearchEvent.News
import kekmech.ru.feature_search.mvi.SearchEvent.Wish

internal class SearchReducer : BaseReducer<SearchState, SearchEvent, SearchEffect, SearchAction> {

    override fun reduce(
        event: SearchEvent,
        state: SearchState
    ): Result<SearchState, SearchEffect, SearchAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: SearchState
    ): Result<SearchState, SearchEffect, SearchAction> {
        TODO()
    }

    private fun reduceWish(
        event: Wish,
        state: SearchState
    ): Result<SearchState, SearchEffect, SearchAction> = when (event) {
        is Wish.Init -> Result(state = state)
    }
}