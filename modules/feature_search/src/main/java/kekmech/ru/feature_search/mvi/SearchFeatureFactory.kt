package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.common_mvi.Feature
import kekmech.ru.feature_search.item.FilterItem
import kekmech.ru.feature_search.item.FilterItemType

internal class SearchFeatureFactory(
    private val actor: SearchActor
) {

    fun create(query: String, filter: String): Feature<SearchState, SearchEvent, SearchEffect> {
        val filterType = FilterItemType.valueOf(filter.toUpperCase())
        return BaseFeature(
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