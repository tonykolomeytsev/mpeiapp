package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseFeature

internal class SearchFeatureFactory(
    private val actor: SearchActor
) {

    fun create(query: String) = BaseFeature(
        initialState = SearchState(query),
        reducer = SearchReducer(),
        actor = actor
    ).start()
}