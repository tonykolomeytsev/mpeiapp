package kekmech.ru.feature_app_settings.screens.favorites.elm

import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesCommand as Command
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEffect as Effect
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent as Event
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesState as State

internal class FavoritesReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.LoadAllFavoritesSuccess -> state { copy(favorites = event.favorites) }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> commands { +Command.LoadAllFavorites }
            is Ui.Action.UpdateFavorite -> {
                val newFavorites = state.favorites?.updateOrAdd(event.favoriteSchedule)
                state { copy(favorites = newFavorites) }
                commands { +Command.UpdateOrInsertFavorite(event.favoriteSchedule) }
            }
            is Ui.Click.DeleteFavorite -> {
                val newFavorites = state.favorites?.filterNot {
                    it.name.equals(event.favoriteSchedule.name, ignoreCase = true)
                }
                state { copy(favorites = newFavorites) }
                commands { +Command.DeleteFavorite(event.favoriteSchedule) }
            }
        }

    private fun List<FavoriteSchedule>.updateOrAdd(favoriteSchedule: FavoriteSchedule): List<FavoriteSchedule> {
        return if (any { it.name.equals(favoriteSchedule.name, ignoreCase = true) }) {
            map {
                if (it.name.equals(
                        favoriteSchedule.name,
                        ignoreCase = true
                    )
                ) favoriteSchedule else it
            }
        } else {
            this + favoriteSchedule
        }
    }
}
