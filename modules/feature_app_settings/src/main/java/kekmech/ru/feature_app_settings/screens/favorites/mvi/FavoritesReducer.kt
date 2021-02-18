package kekmech.ru.feature_app_settings.screens.favorites.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent.News
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent.Wish

internal class FavoritesReducer : BaseReducer<FavoritesState, FavoritesEvent, FavoritesEffect, FavoritesAction> {

    override fun reduce(
        event: FavoritesEvent,
        state: FavoritesState
    ): Result<FavoritesState, FavoritesEffect, FavoritesAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceWish(
        event: Wish,
        state: FavoritesState
    ): Result<FavoritesState, FavoritesEffect, FavoritesAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            action = FavoritesAction.LoadAllFavorites
        )
        is Wish.Click.AddFavorite -> Result(state = state)
        is Wish.Action.UpdateFavorite -> {
            val newFavorites = state.favorites?.updateOrAdd(event.favoriteSchedule)
            Result(
                state = state.copy(favorites = newFavorites),
                action = newFavorites?.let(FavoritesAction::SetFavorites)
            )
        }
        is Wish.Click.DeleteSchedule -> {
            val newFavorites = state.favorites?.filterNot {
                it.groupNumber.equals(event.favoriteSchedule.groupNumber, ignoreCase = true)
            }
            Result(
                state = state.copy(favorites = newFavorites),
                action = newFavorites?.let(FavoritesAction::SetFavorites)
            )
        }
    }

    private fun reduceNews(
        event: News,
        state: FavoritesState
    ): Result<FavoritesState, FavoritesEffect, FavoritesAction> = when (event) {
        is News.AllFavoritesLoaded -> Result(
            state = state.copy(favorites = event.favorites)
        )
    }

    private fun List<FavoriteSchedule>.updateOrAdd(favoriteSchedule: FavoriteSchedule): List<FavoriteSchedule> {
        return if (any { it.groupNumber.equals(favoriteSchedule.groupNumber, ignoreCase = true) }) {
            map { if (it.groupNumber.equals(favoriteSchedule.groupNumber, ignoreCase = true)) favoriteSchedule else it }
        } else {
            this + favoriteSchedule
        }
    }
}