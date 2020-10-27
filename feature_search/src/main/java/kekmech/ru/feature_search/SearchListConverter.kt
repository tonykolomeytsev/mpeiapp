package kekmech.ru.feature_search

import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_search.mvi.SearchState

internal class SearchListConverter {

    fun map(state: SearchState): List<Any> {
        return when {
            state.query.isEmpty() -> emptyList<Any>()
            else -> mutableListOf<Any>().apply {
                if (state.searchResultsNotes.isNotEmpty()) {
                    add(SpaceItem.VERTICAL_12)
                    add(SectionHeaderItem(titleRes = R.string.search_section_title_notes))
                    add(SpaceItem.VERTICAL_12)
                    addAll(state.searchResultsNotes)
                }
                if (state.searchResultsMap.isNotEmpty()) {
                    add(SpaceItem.VERTICAL_12)
                    add(SectionHeaderItem(titleRes = R.string.search_section_title_map))
                    add(SpaceItem.VERTICAL_12)
                    addAll(state.searchResultsMap)
                }
            }
        }
    }
}