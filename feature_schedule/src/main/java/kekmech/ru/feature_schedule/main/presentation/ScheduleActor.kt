package kekmech.ru.feature_schedule.main.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository

class ScheduleActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<ScheduleAction, ScheduleEvent> {

    override fun execute(action: ScheduleAction): Observable<ScheduleEvent> = when (action) {
        is ScheduleAction.LoadSchedule -> scheduleRepository
            .loadSchedule(weekOffset = action.weekOffset)
            .mapEvents(
                successEventMapper = { ScheduleEvent.News.ScheduleWeekLoadSuccess(action.weekOffset, it) },
                failureEvent = ScheduleEvent.News::ScheduleWeekLoadError
            )
    }
}