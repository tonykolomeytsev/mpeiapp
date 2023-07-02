package kekmech.ru.feature_app_settings.screens.favorites.elm

import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import kekmech.ru.library_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesCommand as Command
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent as Event

internal class FavoritesActor(
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.GetAllFavorites -> actorFlow { favoriteScheduleRepository.getAllFavorites() }
                .mapEvents(Internal::GetAllFavoritesSuccess)

            is Command.UpdateOrInsertFavorite -> actorFlow {
                favoriteScheduleRepository.updateOrInsertFavorite(
                    command.favoriteSchedule
                )
            }.mapEvents()

            is Command.DeleteFavorite -> actorFlow {
                favoriteScheduleRepository.deleteFavorite(
                    command.favoriteSchedule
                )
            }.mapEvents()
        }
}
