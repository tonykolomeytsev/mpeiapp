package kekmech.ru.feature_search.screens.schedule_details.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxCompletable
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadSchedule -> scheduleRepository
                .getSchedule(command.type, command.name, command.weekOffset)
                .mapSuccessEvent { Internal.LoadScheduleSuccess(it, command.weekOffset) }
            is Command.GetFavorites -> rxSingle(Dispatchers.Unconfined) {
                favoriteScheduleRepository.getAllFavorites()
            }.mapSuccessEvent(Internal::GetFavoritesSuccess)
            is Command.AddToFavorites -> rxCompletable(Dispatchers.Unconfined) {
                favoriteScheduleRepository.updateOrInsertFavorite(command.schedule)
            }.mapSuccessEvent(Internal.AddToFavoritesSuccess(command.schedule))
            is Command.RemoveFromFavorites -> rxCompletable(Dispatchers.Unconfined) {
                favoriteScheduleRepository.deleteFavorite(command.schedule)
            }.mapSuccessEvent(Internal.RemoveFromFavoritesSuccess)
            is Command.SwitchSchedule -> scheduleRepository
                .setSelectedSchedule(command.selectedSchedule)
                .toObservable()
        }
}
