package kekmech.ru.feature_app_settings.screens.favorites.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.News
import vivid.money.elmslie.core.store.Actor

internal class FavoritesActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<FavoritesAction, FavoritesEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: FavoritesAction): Observable<FavoritesEvent> = when (action) {
        is FavoritesAction.LoadAllFavorites -> scheduleRepository.getFavorites()
            .mapSuccessEvent(News::AllFavoritesLoaded)
        is FavoritesAction.SetFavorites -> scheduleRepository.setFavorites(action.favorites)
            .toObservable()
    }
}