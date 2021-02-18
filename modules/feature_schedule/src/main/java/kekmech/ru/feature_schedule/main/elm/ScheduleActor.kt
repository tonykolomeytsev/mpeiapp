package kekmech.ru.feature_schedule.main.elm

import io.reactivex.Observable
import kekmech.ru.domain_notes.NotesScheduleTransformer
import kekmech.ru.domain_schedule.ScheduleRepository
import vivid.money.elmslie.core.store.Actor

internal class ScheduleActor(
    private val scheduleRepository: ScheduleRepository,
    private val notesScheduleTransformer: NotesScheduleTransformer
) : Actor<ScheduleAction, ScheduleEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: ScheduleAction): Observable<ScheduleEvent> = when (action) {
        is ScheduleAction.LoadSchedule -> scheduleRepository
            .loadSchedule(weekOffset = action.weekOffset)
            .flatMap(notesScheduleTransformer::transform)
            .mapEvents(
                successEventMapper = { ScheduleEvent.News.ScheduleWeekLoadSuccess(action.weekOffset, it) },
                failureEventMapper = ScheduleEvent.News::ScheduleWeekLoadError
            )
    }
}