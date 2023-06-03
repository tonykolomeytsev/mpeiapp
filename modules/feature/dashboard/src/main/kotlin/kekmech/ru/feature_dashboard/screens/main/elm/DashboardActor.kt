package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_dashboard.interactors.GetUpcomingEventsInteractor
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_notes.interactors.GetActualNotesInteractor
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsInteractor: GetUpcomingEventsInteractor,
    private val getActualNotesInteractor: GetActualNotesInteractor,
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.GetSelectedSchedule -> actorFlow {
                scheduleRepository.getSelectedSchedule().await()
            }.mapEvents(Internal::GetSelectedScheduleSuccess)

            is Command.GetWeekOfSemester -> actorFlow {
                getCurrentScheduleUseCase
                    .getSchedule(weekOffset = 0)
                    .map { it.weeks.first().weekOfSemester }
                    .await()
            }.mapEvents(
                eventMapper = Internal::GetWeekOfSemesterSuccess,
                errorMapper = Internal::GetWeekOfSemesterFailure,
            )

            is Command.GetUpcomingEvents -> actorFlow {
                getUpcomingEventsInteractor.invoke()
            }.mapEvents(
                eventMapper = Internal::GetUpcomingEventsSuccess,
                errorMapper = Internal::GetUpcomingEventsFailure,
            )

            is Command.GetActualNotes -> actorFlow {
                getActualNotesInteractor.invoke()
            }.mapEvents(
                eventMapper = Internal::GetActualNotesSuccess,
                errorMapper = Internal::GetActualNotesFailure,
            )

            is Command.GetFavoriteSchedules -> actorFlow {
                favoriteScheduleRepository.getAllFavorites()
            }.mapEvents(
                eventMapper = Internal::GetFavoriteSchedulesSuccess,
                errorMapper = Internal::GetFavoriteSchedulesFailure,
            )

            is Command.SelectSchedule -> actorFlow {
                scheduleRepository
                    .setSelectedSchedule(command.selectedSchedule).await()
            }.mapEvents({ Internal.SelectScheduleSuccess })
        }
}
