package kekmech.ru.feature_search_impl.screens.main

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.item.FilterItemType.GROUPS
import kekmech.ru.feature_search_impl.item.FilterItemType.MAP
import kekmech.ru.feature_search_impl.item.FilterItemType.NOTES
import kekmech.ru.feature_search_impl.item.FilterItemType.PERSONS
import kekmech.ru.feature_search_impl.item.compareFilter
import kekmech.ru.feature_search_impl.screens.main.elm.SearchState
import kekmech.ru.res_strings.R.string as Strings

@Immutable
internal sealed interface SearchListItem {
    val key: String

    data class Header(@StringRes val titleRes: Int) : SearchListItem {
        override val key: String get() = "header_$titleRes"
    }

    data class NoteEntry(val note: Note) : SearchListItem {
        override val key: String get() = "note_${note.id ?: note.hashCode()}"
    }

    data class MapMarkerEntry(val marker: MapMarker) : SearchListItem {
        override val key: String get() = "marker_${marker.uid}"
    }

    data class ScheduleEntry(val result: SearchResult) : SearchListItem {
        override val key: String get() = "schedule_${result.type}_${result.name}"
    }
}

internal sealed interface SearchListState {
    data object EmptyQuery : SearchListState
    data object NotFound : SearchListState
    data class Results(val items: List<SearchListItem>) : SearchListState
}

internal class SearchListConverter {

    fun map(state: SearchState): SearchListState {
        if (state.query.isEmpty()) return SearchListState.EmptyQuery
        val items = buildList {
            if (state.selectedFilter.compareFilter(NOTES)) addResults(
                titleRes = Strings.search_section_title_notes,
                items = state.searchResultsNotes.map(SearchListItem::NoteEntry),
            )
            if (state.selectedFilter.compareFilter(PERSONS)) addResults(
                titleRes = Strings.search_filter_persons,
                items = state.searchResultsPersons.map(SearchListItem::ScheduleEntry),
            )
            if (state.selectedFilter.compareFilter(MAP)) addResults(
                titleRes = Strings.search_section_title_map,
                items = state.searchResultsMap.map(SearchListItem::MapMarkerEntry),
            )
            if (state.selectedFilter.compareFilter(GROUPS)) addResults(
                titleRes = Strings.search_filter_groups,
                items = state.searchResultsGroups.map(SearchListItem::ScheduleEntry),
            )
        }
        return if (items.isEmpty()) SearchListState.NotFound else SearchListState.Results(items)
    }

    private fun MutableList<SearchListItem>.addResults(
        @StringRes titleRes: Int,
        items: List<SearchListItem>,
    ) {
        if (items.isEmpty()) return
        add(SearchListItem.Header(titleRes))
        addAll(items)
    }
}
