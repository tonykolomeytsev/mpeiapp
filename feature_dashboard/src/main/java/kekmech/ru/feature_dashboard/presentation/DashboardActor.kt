package kekmech.ru.feature_dashboard.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News

class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadSchedule -> scheduleRepository.loadSchedule(weekOffset = action.weekOffset)
            .mapEvents({ News.ScheduleLoaded(it, action.weekOffset) }, News::ScheduleLoadError)
        is DashboardAction.GetSelectedGroupName -> scheduleRepository.getSelectedGroup()
            .mapSuccessEvent(News::SelectedGroupNameLoaded)
        is DashboardAction.LoadNotes -> notesRepository.getNotes()
            .mapEvents(News::NotesLoaded, News::NotesLoadError)
    }
}