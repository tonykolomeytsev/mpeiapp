package kekmech.ru.feature_search.screens.main.elm

import kekmech.ru.feature_search.item.FilterItemType
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store

internal class SearchFeatureFactory(
    private val actor: SearchActor,
) {

    fun create(
        query: String,
        filter: String,
    ): Store<SearchEvent, SearchEffect, SearchState> {
        val filterType = FilterItemType.valueOf(filter.uppercase())
        return ElmStore(
            initialState = SearchState(
                query = query,
                selectedFilter = filterType,
            ),
            reducer = SearchReducer(),
            actor = actor,
        )
    }
}
