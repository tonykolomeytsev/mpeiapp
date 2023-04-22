package kekmech.ru.feature_app_settings.screens.favorites.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import vivid.money.elmslie.core.store.Actor

internal class FavoritesActor(
    private val scheduleRepository: ScheduleRepository,
) : Actor<FavoritesCommand, FavoritesEvent> {

    override fun execute(command: FavoritesCommand): Observable<FavoritesEvent> =
        when (command) {
            is FavoritesCommand.LoadAllFavorites -> scheduleRepository.getFavorites()
                .mapSuccessEvent(Internal::LoadAllFavoritesSuccess)
            is FavoritesCommand.SetFavorites -> scheduleRepository.setFavorites(command.favorites)
                .toObservable()
        }
}
