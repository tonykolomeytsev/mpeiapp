package kekmech.ru.feature_app_settings.screens.favorites.mvi

import kekmech.ru.common_mvi.Feature

internal typealias FavoritesFeature = Feature<FavoritesState, FavoritesEvent, FavoritesEffect>

internal data class FavoritesState(
    val isLoading: Boolean
)

internal sealed class FavoritesEvent {

    sealed class Wish : FavoritesEvent() {
        object Init : Wish()
    }

    sealed class News : FavoritesEvent() {

    }
}

internal sealed class FavoritesEffect {

}

internal sealed class FavoritesAction {

}