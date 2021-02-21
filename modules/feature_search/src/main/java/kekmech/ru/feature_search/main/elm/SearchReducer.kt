package kekmech.ru.feature_search.main.elm

import kekmech.ru.feature_search.item.FilterItemType
import kekmech.ru.feature_search.item.FilterItemType.*
import kekmech.ru.feature_search.item.compareFilter
import kekmech.ru.feature_search.main.elm.SearchEvent.News
import kekmech.ru.feature_search.main.elm.SearchEvent.Wish
import kekmech.ru.feature_search.main.simplify
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result

internal class SearchReducer : StateReducer<SearchEvent, SearchState, SearchEffect, SearchAction> {

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
        is News.SearchNotesSuccess -> Result(state.copy(searchResultsNotes = event.results))
        is News.SearchMapSuccess -> Result(state.copy(searchResultsMap = event.results))
        is News.SearchGroupsSuccess -> Result(state.copy(searchResultsGroups = event.results))
        is News.SearchPersonsSuccess -> Result(state.copy(searchResultsPersons = event.results))
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
                    commands = getLoadActions(simplifiedQuery, state.selectedFilter)
                )
            }
        }
        is Wish.Action.SearchContent -> {
            val simplifiedQuery = event.query.simplify()
            Result(
                state = state.copy(query = event.query),
                commands = getLoadActions(simplifiedQuery, state.selectedFilter)
            )
        }
        is Wish.Action.SelectFilter -> {
            val newFilterItems = state.filterItems.map { it.copy(isSelected = it.type == event.filterItem.type) }
            val newSelectedFilter = event.filterItem.type
            val newState = state.copy(
                filterItems = newFilterItems,
                selectedFilter = newSelectedFilter
            )
            Result(
                state = newState,
                commands = getLoadActions(newState.query.simplify(), newState.selectedFilter)
            )
        }
    }

    private fun getLoadActions(simplifiedQuery: String, selectedFilter: FilterItemType): List<SearchAction> {
        return listOfNotNull(
            SearchAction.SearchNotes(simplifiedQuery).takeIf { selectedFilter.compareFilter(NOTES) },
            SearchAction.SearchMap(simplifiedQuery).takeIf { selectedFilter.compareFilter(MAP) },
            SearchAction.SearchGroups(simplifiedQuery).takeIf { selectedFilter.compareFilter(GROUPS) },
            SearchAction.SearchPersons(simplifiedQuery).takeIf { selectedFilter.compareFilter(PERSONS) }
        ).takeIf { simplifiedQuery.isNotEmpty() } ?: emptyList()
    }
}