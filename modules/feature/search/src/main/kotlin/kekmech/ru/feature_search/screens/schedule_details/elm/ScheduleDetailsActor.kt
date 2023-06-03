package kekmech.ru.feature_search.screens.schedule_details.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent as Event

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadSchedule -> actorFlow {
                scheduleRepository
                    .getSchedule(
                        type = command.type,
                        name = command.name,
                        weekOffset = command.weekOffset,
                    )
                    .getOrThrow()
            }.mapEvents({ Internal.LoadScheduleSuccess(it, command.weekOffset) })

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