package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.ObserveSchedule -> scheduleRepository
                .observeSchedule(ScheduleKey.of(command.type, command.name, command.weekOffset))
                .mapEvents(
                    eventMapper = { Internal.ScheduleUpdated(command.weekOffset, it) },
                    errorMapper = { Internal.ObserveScheduleFailure(command.weekOffset, it) },
                )
                .switch(key = command.weekOffset)

            is Command.RefreshSchedule -> actorFlow {
                // failures are delivered to the subscribers as ScheduleRefreshState.Failed
                scheduleRepository.refreshSchedule(command.key)
            }.mapEvents()

            is Command.GetFavorites -> actorFlow {
                favoriteScheduleRepository.getAllFavorites()
            }.mapEvents(Internal::GetFavoritesSuccess)

            is Command.AddToFavorites -> actorFlow {
                favoriteScheduleRepository.updateOrInsertFavorite(command.schedule)
            }.mapEvents({ Internal.AddToFavoritesSuccess(command.schedule) })

            is Command.RemoveFromFavorites -> actorFlow {
                favoriteScheduleRepository.deleteFavorite(command.schedule)
            }.mapEvents({ Internal.RemoveFromFavoritesSuccess })
        }
}
