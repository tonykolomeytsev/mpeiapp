package kekmech.ru.feature_search_impl.screens.main.elm

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.item.FilterItem
import kekmech.ru.feature_search_impl.item.FilterItemType

@Immutable
internal data class SearchState(
    val query: String,
    val searchResultsMap: List<MapMarker> = emptyList(),
    val searchResultsNotes: List<Note> = emptyList(),
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
        data class SearchNotesSuccess(val results: List<Note>) : Internal
        data class SearchMapSuccess(val results: List<MapMarker>) : Internal
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
