package kekmech.ru.feature_app_settings.screens.favorites.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxCompletable
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesCommand as Command
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent as Event

internal class FavoritesActor(
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.GetAllFavorites -> rxSingle(Dispatchers.Unconfined) {
                favoriteScheduleRepository.getAllFavorites()
            }.mapSuccessEvent(Internal::GetAllFavoritesSuccess)
            is Command.UpdateOrInsertFavorite -> rxCompletable(Dispatchers.Unconfined) {
                favoriteScheduleRepository.updateOrInsertFavorite(command.favoriteSchedule)
            }.toObservable()
            is Command.DeleteFavorite -> rxCompletable(Dispatchers.Unconfined) {
                favoriteScheduleRepository.deleteFavorite(command.favoriteSchedule)
            }.toObservable()
        }
}
