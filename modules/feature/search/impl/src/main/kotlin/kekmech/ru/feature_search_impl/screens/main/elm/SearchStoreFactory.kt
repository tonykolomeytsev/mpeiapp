package kekmech.ru.feature_search_impl.screens.main.elm

import kekmech.ru.feature_search_impl.item.FilterItemType
import money.vivid.elmslie.core.store.Store
import money.vivid.elmslie.core.store.ElmStore
internal class SearchStoreFactory(
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
            startEvent = SearchEvent.Ui.Init,
        )
    }
}
