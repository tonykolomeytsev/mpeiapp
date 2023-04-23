package kekmech.ru.feature_search.schedule_details.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadSchedule -> scheduleRepository
                .loadSchedule(command.ownerName, command.weekOffset)
                .mapSuccessEvent { Internal.LoadScheduleSuccess(it, command.weekOffset) }
            is Command.LoadFavorites -> scheduleRepository.getFavorites()
                .mapSuccessEvent(Internal::LoadFavoritesSuccess)
            is Command.AddToFavorites -> scheduleRepository
                .addFavorite(command.schedule)
                .mapSuccessEvent(Internal.AddToFavoritesSuccess(command.schedule))
            is Command.RemoveFromFavorites -> scheduleRepository
                .removeFavorite(command.schedule)
                .mapSuccessEvent(Internal.RemoveFromFavoritesSuccess)
            is Command.SwitchSchedule -> scheduleRepository
                .selectSchedule(command.scheduleName)
                .toObservable()
        }
}
