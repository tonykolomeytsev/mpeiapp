package kekmech.ru.feature_search_impl.screens.main.elm

import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.feature_search_impl.item.FilterItem
import kekmech.ru.feature_search_impl.item.FilterItemType

internal data class SearchState(
    val query: String,
    val searchResultsMap: List<Any> = emptyList(),
    val searchResultsNotes: List<Any> = emptyList(),
    val searchResultsGroups: List<SearchResult> = emptyList(),
    val searchResultsPersons: List<SearchResult> = emptyList(),
    val selectedFilter: FilterItemType = FilterItemType.ALL,
) {
    val filterItems: List<FilterItem> = FilterItem
        .resolveAllItems().map { it.copy(isSelected = it.type == selectedFilter) }
}

internal sealed interface SearchEvent {

    sealed interface Ui : SearchEvent {
        object Init : Ui

        object Action {
            data class SearchContent(val query: String) : Ui
            data class SelectFilter(val filterItem: FilterItem) : Ui
        }
    }

    sealed interface Internal : SearchEvent {
        data class SearchNotesSuccess(val results: List<Any>) : Internal
        data class SearchMapSuccess(val results: List<Any>) : Internal
        data class SearchGroupsSuccess(val results: List<SearchResult>) : Internal
        data class SearchPersonsSuccess(val results: List<SearchResult>) : Internal
    }
}

internal sealed interface SearchEffect {
    data class SetInitialQuery(val query: String) : SearchEffect
}

internal sealed interface SearchCommand {
    data class SearchNotes(val query: String) : SearchCommand
    data class SearchMap(val query: String) : SearchCommand
    data class SearchGroups(val query: String) : SearchCommand
    data class SearchPersons(val query: String) : SearchCommand
}
