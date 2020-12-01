package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.feature_search.item.FilterItem

internal class SearchFeatureFactory(
    private val actor: SearchActor
) {

    fun create(query: String) = BaseFeature(
        initialState = SearchState(query, filterItems = FilterItem.resolveAllItems()),
        reducer = SearchReducer(),
        actor = actor
    ).start()
}