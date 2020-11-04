package kekmech.ru.feature_app_settings.screens.favorites.mvi

import kekmech.ru.common_mvi.BaseFeature

internal class FavoritesFeatureFactory(
    private val actor: FavoritesActor
) {

    fun create() = BaseFeature(
        initialState = FavoritesState(false),
        reducer = FavoritesReducer(),
        actor = actor
    ).start()
}