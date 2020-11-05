package kekmech.ru.feature_app_settings.screens.favorites.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent.News

internal class FavoritesActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<FavoritesAction, FavoritesEvent> {

    override fun execute(action: FavoritesAction): Observable<FavoritesEvent> = when (action) {
        is FavoritesAction.LoadAllFavorites -> scheduleRepository.getFavorites()
            .mapSuccessEvent(News::AllFavoritesLoaded)
        is FavoritesAction.SetFavorites -> scheduleRepository.setFavorites(action.favorites)
            .toObservable()
    }
}