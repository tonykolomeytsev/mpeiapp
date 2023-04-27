package kekmech.ru.feature_dashboard.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_dashboard.use_cases.GetSelectedScheduleMetaInfoUseCase
import kekmech.ru.domain_dashboard.use_cases.GetUpcomingEventsUseCase
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import vivid.money.elmslie.core.store.Actor

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val getSelectedScheduleMetaInfoUseCase: GetSelectedScheduleMetaInfoUseCase,
    private val getActualNotesUseCase: GetActualNotesUseCase,
) : Actor<DashboardCommand, DashboardEvent> {

    override fun execute(command: DashboardCommand): Observable<DashboardEvent> =
        when (command) {
            is DashboardCommand.GetSelectedScheduleMetaInfo -> getSelectedScheduleMetaInfoUseCase
                .getScheduleMetaInfo()
                .mapEvents(
                    successEventMapper = Internal::GetSelectedScheduleMetaInfoSuccess,
                    failureEventMapper = Internal::GetSelectedScheduleMetaInfoFailure,
                )
            is DashboardCommand.GetUpcomingEvents -> getUpcomingEventsUseCase
                .getPrediction()
                .mapEvents(
                    successEventMapper = Internal::GetUpcomingEventsSuccess,
                    failureEventMapper = Internal::GetUpcomingEventsFailure,
                )
            is DashboardCommand.GetActualNotes -> getActualNotesUseCase
                .getActualNotes()
                .mapEvents(
                    successEventMapper = Internal::GetActualNotesSuccess,
                    failureEventMapper = Internal::GetActualNotesFailure,
                )
            is DashboardCommand.GetFavoriteSchedules -> favoriteScheduleRepository
                .getFavorites()
                .mapEvents(
                    successEventMapper = Internal::GetFavoriteSchedulesSuccess,
                    failureEventMapper = Internal::GetFavoriteSchedulesFailure,
                )
            is DashboardCommand.SelectGroup -> scheduleRepository.selectSchedule(command.groupName)
                .mapSuccessEvent(Internal.SelectGroupSuccess)
        }
}
