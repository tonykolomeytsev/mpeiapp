package kekmech.ru.feature_app_settings.screens.favorites.elm

import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor

internal class FavoritesActor(
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<FavoritesCommand, FavoritesEvent>() {

    override fun execute(command: FavoritesCommand): Flow<FavoritesEvent> =
        when (command) {
            is FavoritesCommand.LoadAllFavorites -> actorFlow {
                favoriteScheduleRepository.getAllFavorites().await()
            }.mapEvents(Internal::LoadAllFavoritesSuccess)

            is FavoritesCommand.UpdateOrInsertFavorite -> actorEmptyFlow {
                favoriteScheduleRepository
                    .updateOrInsertFavorite(command.favoriteSchedule)
                    .await()
            }

            is FavoritesCommand.DeleteFavorite -> actorEmptyFlow {
                favoriteScheduleRepository
                    .deleteFavorite(command.favoriteSchedule)
                    .await()
            }
        }
}
