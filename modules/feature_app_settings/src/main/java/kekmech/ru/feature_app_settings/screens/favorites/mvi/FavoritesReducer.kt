package kekmech.ru.feature_app_settings.screens.favorites.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
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
        is Wish.Init -> Result(state = state)
    }

    private fun reduceNews(
        event: News,
        state: FavoritesState
    ): Result<FavoritesState, FavoritesEffect, FavoritesAction> {
        TODO("Not yet implemented")
    }
}