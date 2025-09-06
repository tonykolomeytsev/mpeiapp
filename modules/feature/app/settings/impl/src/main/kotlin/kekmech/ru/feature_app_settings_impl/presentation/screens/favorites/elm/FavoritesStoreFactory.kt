package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm

import money.vivid.elmslie.core.store.ElmStore

internal class FavoritesStoreFactory(
    private val actor: FavoritesActor,
) {

    fun create() =
        ElmStore(
            initialState = FavoritesState(),
            reducer = FavoritesReducer(),
            actor = actor,
            startEvent = FavoritesEvent.Ui.Init,
        )
}
