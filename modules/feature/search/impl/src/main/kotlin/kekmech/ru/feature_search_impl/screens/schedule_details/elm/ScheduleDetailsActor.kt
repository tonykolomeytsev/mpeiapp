package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadSchedule -> scheduleRepository
                .observeSchedule(ScheduleKey.of(command.type, command.name, command.weekOffset))
                .mapNotNull { resource ->
                    val schedule = resource.value
                    when {
                        schedule != null -> Internal.LoadScheduleSuccess(schedule, command.weekOffset)
                        resource.error != null -> Internal.LoadScheduleError(command.weekOffset)
                        else -> null // still loading
                    }
                }
                .mapEvents(
                    eventMapper = { it },
                    errorMapper = { Internal.LoadScheduleError(command.weekOffset) },
                )
                .switch(key = command.weekOffset)

            is Command.GetFavorites -> actorFlow {
                favoriteScheduleRepository.getAllFavorites()
            }.mapEvents(Internal::GetFavoritesSuccess)

            is Command.AddToFavorites -> actorFlow {
                favoriteScheduleRepository.updateOrInsertFavorite(command.schedule)
            }.mapEvents({ Internal.AddToFavoritesSuccess(command.schedule) })

            is Command.RemoveFromFavorites -> actorFlow {
                favoriteScheduleRepository.deleteFavorite(command.schedule)
            }.mapEvents({ Internal.RemoveFromFavoritesSuccess })

            is Command.SwitchSchedule -> actorFlow {
                scheduleRepository.setSelectedSchedule(command.selectedSchedule)
            }.mapEvents()
        }
}
