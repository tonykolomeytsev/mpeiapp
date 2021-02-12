package kekmech.ru.feature_search

import androidx.annotation.StringRes
import kekmech.ru.coreui.items.BottomLabeledTextItem
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_search.item.FilterItemType
import kekmech.ru.feature_search.item.FilterItemType.*
import kekmech.ru.feature_search.item.compareFilter
import kekmech.ru.feature_search.mvi.SearchState

internal class SearchListConverter {

    fun map(state: SearchState): List<Any> {
        return when {
            state.query.isEmpty() -> listOf(
                EmptyStateItem(
                    titleRes = R.string.search_empty_state_title,
                    subtitleRes = R.string.search_empty_state_subtitle
                )
            )
            else -> mutableListOf<Any>().apply {
                if (state.selectedFilter.compareFilter(NOTES)) addResults(
                    titleRes = R.string.search_section_title_notes,
                    items = state.searchResultsNotes
                )
                if (state.selectedFilter.compareFilter(PERSONS)) addResults(
                    titleRes = R.string.search_filter_persons,
                    items = state.searchResultsPersons.map { BottomLabeledTextItem(mainText = it.name, label = it.description) }
                )
                if (state.selectedFilter.compareFilter(MAP)) addResults(
                    titleRes = R.string.search_section_title_map,
                    items = state.searchResultsMap
                )
                if (state.selectedFilter.compareFilter(GROUPS)) addResults(
                    titleRes = R.string.search_filter_groups,
                    items = state.searchResultsGroups.map { BottomLabeledTextItem(mainText = it.name, label = it.description) }
                )
            }
                .takeIf { it.isNotEmpty() }
                ?: listOf(EmptyStateItem(
                    titleRes = R.string.search_not_found_state_title,
                    subtitleRes = R.string.search_not_found_state_subtitle
                ))
        }
    }

    private fun MutableList<Any>.addResults(@StringRes titleRes: Int, items: List<Any>) {
        if (items.isEmpty()) return
        add(SpaceItem.VERTICAL_12)
        add(SectionHeaderItem(titleRes = titleRes))
        add(SpaceItem.VERTICAL_12)
        addAll(items)
    }
}