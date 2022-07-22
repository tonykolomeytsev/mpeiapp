package kekmech.ru.feature_dashboard.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.NotesScheduleTransformer
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.elm.DashboardEvent.News
import vivid.money.elmslie.core.store.Actor

class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
    private val notesScheduleTransformer: NotesScheduleTransformer,
) : Actor<DashboardAction, DashboardEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadSchedule -> scheduleRepository.loadSchedule(weekOffset = action.weekOffset)
            .flatMap(notesScheduleTransformer::transform)
            .mapEvents({ News.ScheduleLoaded(it, action.weekOffset) }, News::ScheduleLoadError)
        is DashboardAction.GetSelectedGroupName -> scheduleRepository.getSelectedScheduleName()
            .mapSuccessEvent(News::SelectedGroupNameLoaded)
        is DashboardAction.LoadNotes -> notesRepository.getNotes()
            .mapEvents(News::NotesLoaded, News::NotesLoadError)
        is DashboardAction.LoadFavoriteSchedules -> scheduleRepository.getFavorites()
            .mapSuccessEvent(News::FavoriteSchedulesLoaded)
        is DashboardAction.SelectGroup -> scheduleRepository.selectSchedule(action.groupName)
            .mapSuccessEvent(News.FavoriteGroupSelected)
        is DashboardAction.LoadSession -> scheduleRepository.getSession()
            .mapSuccessEvent { News.SessionLoaded(it.items) }
    }
}
