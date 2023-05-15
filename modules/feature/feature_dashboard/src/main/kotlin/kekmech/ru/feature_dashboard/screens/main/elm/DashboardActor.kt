package kekmech.ru.feature_dashboard.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_dashboard.interactors.GetUpcomingEventsInteractor
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_notes.interactors.GetActualNotesInteractor
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsInteractor: GetUpcomingEventsInteractor,
    private val getActualNotesInteractor: GetActualNotesInteractor,
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
            is Command.GetUpcomingEvents -> rxSingle(Dispatchers.Unconfined) {
                getUpcomingEventsInteractor.invoke()
            }
                .mapEvents(
                    successEventMapper = Internal::GetUpcomingEventsSuccess,
                    failureEventMapper = Internal::GetUpcomingEventsFailure,
                )
            is Command.GetActualNotes -> rxSingle(Dispatchers.Unconfined) {
                getActualNotesInteractor.invoke()
            }
                .mapEvents(
                    successEventMapper = Internal::GetActualNotesSuccess,
                    failureEventMapper = Internal::GetActualNotesFailure,
                )
            is Command.GetFavoriteSchedules -> rxSingle(Dispatchers.Unconfined) {
                favoriteScheduleRepository.getAllFavorites()
            }
                .mapEvents(
                    successEventMapper = Internal::GetFavoriteSchedulesSuccess,
                    failureEventMapper = Internal::GetFavoriteSchedulesFailure,
                )
            is Command.SelectSchedule -> scheduleRepository
                .setSelectedSchedule(command.selectedSchedule)
                .mapSuccessEvent(Internal.SelectScheduleSuccess)
        }
}
