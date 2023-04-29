package kekmech.ru.feature_dashboard.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_dashboard.use_cases.GetUpcomingEventsUseCase
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val getActualNotesUseCase: GetActualNotesUseCase,
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.GetSelectedSchedule -> scheduleRepository.getSelectedSchedule()
                .mapSuccessEvent(Internal::GetSelectedScheduleSuccess)
            is Command.GetWeekOfSemester -> getCurrentScheduleUseCase
                .getSchedule(weekOffset = 0)
                .map { it.weeks.first().weekOfSemester }
                .mapEvents(
                    successEventMapper = Internal::GetWeekOfSemesterSuccess,
                    failureEventMapper = Internal::GetWeekOfSemesterFailure,
                )
            is Command.GetUpcomingEvents -> getUpcomingEventsUseCase
                .getPrediction()
                .mapEvents(
                    successEventMapper = Internal::GetUpcomingEventsSuccess,
                    failureEventMapper = Internal::GetUpcomingEventsFailure,
                )
            is Command.GetActualNotes -> getActualNotesUseCase
                .getActualNotes()
                .mapEvents(
                    successEventMapper = Internal::GetActualNotesSuccess,
                    failureEventMapper = Internal::GetActualNotesFailure,
                )
            is Command.GetFavoriteSchedules -> favoriteScheduleRepository
                .getAllFavorites()
                .mapEvents(
                    successEventMapper = Internal::GetFavoriteSchedulesSuccess,
                    failureEventMapper = Internal::GetFavoriteSchedulesFailure,
                )
            is Command.SelectSchedule -> scheduleRepository
                .setSelectedSchedule(command.selectedSchedule)
                .mapSuccessEvent(Internal.SelectScheduleSuccess)
        }
}
