package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class FavoritesStoreFactory(
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
