package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_search.item.FilterItemType
import kekmech.ru.feature_search.item.FilterItemType.MAP
import kekmech.ru.feature_search.item.FilterItemType.NOTES
import kekmech.ru.feature_search.mvi.SearchEvent.News
import kekmech.ru.feature_search.mvi.SearchEvent.Wish
import kekmech.ru.feature_search.simplify

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
    ): Result<SearchState, SearchEffect, SearchAction> = when (event) {
        is News.SearchNotesSuccess -> Result(
            state = state.copy(searchResultsNotes = event.results)
        )
        is News.SearchMapSuccess -> Result(
            state = state.copy(searchResultsMap = event.results)
        )
    }

    private fun reduceWish(
        event: Wish,
        state: SearchState
    ): Result<SearchState, SearchEffect, SearchAction> = when (event) {
        is Wish.Init -> {
            if (state.query.isEmpty()) {
                Result(state = state)
            } else {
                val simplifiedQuery = state.query.simplify()
                Result(
                    state = state.copy(query = state.query),
                    effects = listOf(SearchEffect.SetInitialQuery(state.query)),
                    actions = getLoadActions(simplifiedQuery, state.selectedFilter)
                )
            }
        }
        is Wish.Action.SearchContent -> {
            val simplifiedQuery = event.query.simplify()
            Result(
                state = state.copy(query = event.query),
                effects = emptyList(),
                actions = getLoadActions(simplifiedQuery, state.selectedFilter)
            )
        }
        is Wish.Action.SelectFilter -> {
            val newFilterItems = state.filterItems.map { it.copy(isSelected = it.type == event.filterItem.type) }
            val newSelectedFilter = event.filterItem.type
            val newState = state.copy(
                filterItems = newFilterItems,
                selectedFilter = newSelectedFilter,
                searchResultsNotes = if (newSelectedFilter.compareFilter(NOTES)) state.searchResultsNotes else emptyList(),
                searchResultsMap = if (newSelectedFilter.compareFilter(MAP)) state.searchResultsMap else emptyList()
            )
            Result(
                state = newState,
                effects = emptyList(),
                actions = getLoadActions(newState.query, newState.selectedFilter)
            )
        }
    }

    private fun getLoadActions(simplifiedQuery: String, selectedFilter: FilterItemType): List<SearchAction> {
        return listOfNotNull(
            SearchAction.SearchNotes(simplifiedQuery).takeIf { selectedFilter.compareFilter(NOTES) },
            SearchAction.SearchMap(simplifiedQuery).takeIf { selectedFilter.compareFilter(MAP) }
        ).takeIf { simplifiedQuery.isNotEmpty() } ?: emptyList()
    }

    private fun FilterItemType.compareFilter(filterItemType: FilterItemType) =
        this == FilterItemType.ALL || this == filterItemType
}