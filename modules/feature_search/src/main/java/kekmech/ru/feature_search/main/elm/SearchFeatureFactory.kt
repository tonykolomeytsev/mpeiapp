package kekmech.ru.feature_search.main.elm

import kekmech.ru.feature_search.item.FilterItem
import kekmech.ru.feature_search.item.FilterItemType
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class SearchFeatureFactory(
    private val actor: SearchActor
) {

    fun create(
        query: String,
        filter: String
    ): Store<SearchEvent, SearchEffect, SearchState> {
        val filterType = FilterItemType.valueOf(filter.toUpperCase())
        return ElmStore(
            initialState = SearchState(
                query,
                filterItems = FilterItem.resolveAllItems().map { it.copy(isSelected = it.type == filterType) },
                selectedFilter = filterType
            ),
            reducer = SearchReducer(),
            actor = actor
        ).start()
    }
}