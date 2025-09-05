package kekmech.ru.feature_search.screens.schedule_details.elm

import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadSchedule -> actorFlow {
                scheduleRepository
                    .getSchedule(command.type, command.name, command.weekOffset)
                    .await()
            }.mapEvents(eventMapper = { Internal.LoadScheduleSuccess(it, command.weekOffset) })

            is Command.LoadFavorites -> actorFlow {
                favoriteScheduleRepository.getAllFavorites().await()
            }.mapEvents(Internal::LoadFavoritesSuccess)

            is Command.AddToFavorites -> actorFlow {
                favoriteScheduleRepository
                    .updateOrInsertFavorite(command.schedule)
                    .await()
            }.mapEvents(eventMapper = { Internal.AddToFavoritesSuccess(command.schedule) })

            is Command.RemoveFromFavorites -> actorFlow {
                favoriteScheduleRepository
                    .deleteFavorite(command.schedule)
                    .await()
            }.mapEvents(eventMapper = { Internal.RemoveFromFavoritesSuccess })

            is Command.SwitchSchedule -> actorEmptyFlow {
                scheduleRepository
                    .setSelectedSchedule(command.selectedSchedule)
                    .await()
            }
        }
}
