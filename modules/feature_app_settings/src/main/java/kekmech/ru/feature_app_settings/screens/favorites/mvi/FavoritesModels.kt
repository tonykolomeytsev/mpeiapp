package kekmech.ru.feature_app_settings.screens.favorites.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.FavoriteSchedule

internal typealias FavoritesFeature = Feature<FavoritesState, FavoritesEvent, FavoritesEffect>

internal data class FavoritesState(
    val favorites: List<FavoriteSchedule>? = null
)

internal sealed class FavoritesEvent {

    sealed class Wish : FavoritesEvent() {
        object Init : Wish()

        object Click {
            object AddFavorite : Wish()
            data class EditFavorite(val favoriteSchedule: FavoriteSchedule) : Wish()
        }
    }

    sealed class News : FavoritesEvent() {
        data class AllFavoritesLoaded(val favorites: List<FavoriteSchedule>) : News()
    }
}

internal sealed class FavoritesEffect

internal sealed class FavoritesAction {
    object LoadAllFavorites : FavoritesAction()
    data class SetFavorites(val favorites: List<FavoriteSchedule>) : FavoritesAction()
}