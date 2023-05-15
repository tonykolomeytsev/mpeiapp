package kekmech.ru.feature_app_settings.screens.favorites.elm

import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule

internal data class FavoritesState(
    val favorites: List<FavoriteSchedule>? = null,
)

internal sealed interface FavoritesEvent {

    sealed interface Ui : FavoritesEvent {
        object Init : Ui

        object Click {
            data class DeleteFavorite(val favoriteSchedule: FavoriteSchedule) : Ui
        }

        object Action {
            data class UpdateFavorite(val favoriteSchedule: FavoriteSchedule) : Ui
        }
    }

    sealed interface Internal : FavoritesEvent {
        data class GetAllFavoritesSuccess(val favorites: List<FavoriteSchedule>) : Internal
    }
}

internal sealed interface FavoritesEffect

internal sealed interface FavoritesCommand {
    object GetAllFavorites : FavoritesCommand
    data class UpdateOrInsertFavorite(val favoriteSchedule: FavoriteSchedule) : FavoritesCommand
    data class DeleteFavorite(val favoriteSchedule: FavoriteSchedule) : FavoritesCommand
}
