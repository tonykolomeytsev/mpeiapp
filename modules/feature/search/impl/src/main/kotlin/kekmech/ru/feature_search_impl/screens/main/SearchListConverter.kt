package kekmech.ru.feature_search_impl.screens.main

import androidx.annotation.StringRes
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_search_impl.item.FilterItemType.GROUPS
import kekmech.ru.feature_search_impl.item.FilterItemType.MAP
import kekmech.ru.feature_search_impl.item.FilterItemType.NOTES
import kekmech.ru.feature_search_impl.item.FilterItemType.PERSONS
import kekmech.ru.feature_search_impl.item.compareFilter
import kekmech.ru.feature_search_impl.screens.main.elm.SearchState
import kekmech.ru.strings.Strings

internal class SearchListConverter {

    fun map(state: SearchState): List<Any> {
        return when {
            state.query.isEmpty() -> listOf(
                EmptyStateItem(
                    titleRes = Strings.search_empty_state_title,
                    subtitleRes = Strings.search_empty_state_subtitle
                )
            )
            else -> mutableListOf<Any>().apply {
                if (state.selectedFilter.compareFilter(NOTES)) addResults(
                    titleRes = Strings.search_section_title_notes,
                    items = state.searchResultsNotes
                )
                if (state.selectedFilter.compareFilter(PERSONS)) addResults(
                    titleRes = Strings.search_filter_persons,
                    items = state.searchResultsPersons
                )
                if (state.selectedFilter.compareFilter(MAP)) addResults(
                    titleRes = Strings.search_section_title_map,
                    items = state.searchResultsMap
                )
                if (state.selectedFilter.compareFilter(GROUPS)) addResults(
                    titleRes = Strings.search_filter_groups,
                    items = state.searchResultsGroups
                )
            }
                .takeIf { it.isNotEmpty() }
                ?: listOf(EmptyStateItem(
                    titleRes = Strings.search_not_found_state_title,
                    subtitleRes = Strings.search_not_found_state_subtitle
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
