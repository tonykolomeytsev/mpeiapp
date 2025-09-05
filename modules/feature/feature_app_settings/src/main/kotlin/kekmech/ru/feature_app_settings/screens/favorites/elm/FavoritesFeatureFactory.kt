package kekmech.ru.feature_app_settings.screens.favorites.elm

import money.vivid.elmslie.core.store.ElmStore

internal class FavoritesFeatureFactory(
    private val actor: FavoritesActor,
) {

    fun create() =
        ElmStore(
            initialState = FavoritesState(),
            reducer = FavoritesReducer(),
            actor = actor,
        )
}
