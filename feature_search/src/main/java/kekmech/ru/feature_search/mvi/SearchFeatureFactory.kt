package kekmech.ru.feature_search.mvi

import kekmech.ru.common_mvi.BaseFeature

internal class SearchFeatureFactory(
    private val actor: SearchActor
) {

    fun create() = BaseFeature(
        initialState = SearchState(),
        reducer = SearchReducer(),
        actor = actor
    ).start()
}