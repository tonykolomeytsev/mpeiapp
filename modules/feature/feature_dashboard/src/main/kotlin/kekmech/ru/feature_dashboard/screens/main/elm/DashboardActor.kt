package kekmech.ru.feature_dashboard.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.NotesScheduleTransformer
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import vivid.money.elmslie.core.store.Actor

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
    private val notesScheduleTransformer: NotesScheduleTransformer,
) : Actor<DashboardCommand, DashboardEvent> {

    override fun execute(command: DashboardCommand): Observable<DashboardEvent> = when (command) {
        is DashboardCommand.LoadSchedule -> scheduleRepository
            .loadSchedule(weekOffset = command.weekOffset)
            .flatMap(notesScheduleTransformer::transform)
            .mapEvents(
                successEventMapper = { Internal.LoadScheduleSuccess(it, command.weekOffset) },
                failureEventMapper = Internal::LoadScheduleFailure,
            )
        is DashboardCommand.GetSelectedGroupName -> scheduleRepository.getSelectedScheduleName()
            .mapSuccessEvent(Internal::GetSelectedGroupNameSuccess)
        is DashboardCommand.LoadNotes -> notesRepository.getNotes()
            .mapEvents(
                successEventMapper = Internal::LoadNotesSuccess,
                failureEventMapper = Internal::LoadNotesFailure,
            )
        is DashboardCommand.LoadFavoriteSchedules -> scheduleRepository.getFavorites()
            .mapSuccessEvent(Internal::LoadFavoriteSchedulesSuccess)
        is DashboardCommand.SelectGroup -> scheduleRepository.selectSchedule(command.groupName)
            .mapSuccessEvent(Internal.SelectGroupSuccess)
        is DashboardCommand.LoadSession -> scheduleRepository.getSession()
            // The error case is intentionally ignored
            .mapSuccessEvent { Internal.LoadSessionSuccess(it.items) }
    }
}
