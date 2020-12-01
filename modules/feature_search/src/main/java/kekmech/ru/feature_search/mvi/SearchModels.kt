package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.feature_search.item.FilterItem
import kekmech.ru.feature_search.item.FilterItemType

internal typealias SearchFeature = Feature<SearchState, SearchEvent, SearchEffect>

internal data class SearchState(
    val query: String,
    val searchResultsMap: List<Any> = emptyList(),
    val searchResultsNotes: List<Any> = emptyList(),
    val filterItems: List<FilterItem>,
    val selectedFilter: FilterItemType = FilterItemType.ALL
)

internal sealed class SearchEvent {

    sealed class Wish : SearchEvent() {
        object Init : Wish()

        object Action {
            data class SearchContent(val query: String) : Wish()
            data class SelectFilter(val filterItem: FilterItem) : Wish()
        }
    }

    sealed class News : SearchEvent() {
        data class SearchNotesSuccess(val results: List<Any>) : News()
        data class SearchMapSuccess(val results: List<Any>) : News()
    }
}

internal sealed class SearchEffect {
    data class SetInitialQuery(val query: String) : SearchEffect()
}

internal sealed class SearchAction {
    data class SearchNotes(val query: String) : SearchAction()
    data class SearchMap(val query: String) : SearchAction()
}
