package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.library_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand as Command
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent as Event

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
