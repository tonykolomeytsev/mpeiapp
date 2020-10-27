package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.Feature

internal typealias SearchFeature = Feature<SearchState, SearchEvent, SearchEffect>

internal data class SearchState(
    val isLoading: Boolean = false,
    val query: String = "",
    val searchResultsMap: List<Any> = emptyList(),
    val searchResultsNotes: List<Any> = emptyList()
)

internal sealed class SearchEvent {

    sealed class Wish : SearchEvent() {
        object Init : Wish()
    }

    sealed class News : SearchEvent() {

    }
}

internal sealed class SearchEffect {

}

internal sealed class SearchAction {

}
