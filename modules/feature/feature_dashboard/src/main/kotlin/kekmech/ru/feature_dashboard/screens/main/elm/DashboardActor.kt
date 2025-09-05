package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_dashboard.use_cases.GetUpcomingEventsUseCase
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val getActualNotesUseCase: GetActualNotesUseCase,
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> = when (command) {
        is Command.GetSelectedSchedule -> actorFlow {
            scheduleRepository.getSelectedSchedule().await()
        }.mapEvents(Internal::GetSelectedScheduleSuccess)

        is Command.GetWeekOfSemester -> actorFlow {
            getCurrentScheduleUseCase.getSchedule(weekOffset = 0)
                .map { it.weeks.first().weekOfSemester }.await()
        }.mapEvents(
            eventMapper = Internal::GetWeekOfSemesterSuccess,
            errorMapper = Internal::GetWeekOfSemesterFailure,
        )

        is Command.GetUpcomingEvents -> actorFlow {
            getUpcomingEventsUseCase.getPrediction().await()
        }.mapEvents(
            eventMapper = Internal::GetUpcomingEventsSuccess,
            errorMapper = Internal::GetUpcomingEventsFailure,
        )

        is Command.GetActualNotes -> actorFlow {
            getActualNotesUseCase.getActualNotes().await()
        }.mapEvents(
            eventMapper = Internal::GetActualNotesSuccess,
            errorMapper = Internal::GetActualNotesFailure,
        )

        is Command.GetFavoriteSchedules -> actorFlow {
            favoriteScheduleRepository.getAllFavorites().await()
        }.mapEvents(
            eventMapper = Internal::GetFavoriteSchedulesSuccess,
            errorMapper = Internal::GetFavoriteSchedulesFailure,
        )

        is Command.SelectSchedule -> actorFlow {
            scheduleRepository.setSelectedSchedule(command.selectedSchedule).await()
        }.mapEvents(eventMapper = { Internal.SelectScheduleSuccess })
    }
}
