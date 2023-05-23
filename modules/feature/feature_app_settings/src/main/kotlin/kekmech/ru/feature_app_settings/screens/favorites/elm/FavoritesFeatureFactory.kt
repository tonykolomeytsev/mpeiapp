package kekmech.ru.feature_app_settings.screens.favorites.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class FavoritesFeatureFactory(
    private val actor: FavoritesActor,
) {

    fun create() =
        ElmStoreCompat(
            initialState = FavoritesState(),
            reducer = FavoritesReducer(),
            actor = actor,
            startEvent = FavoritesEvent.Ui.Init,
        )
}
