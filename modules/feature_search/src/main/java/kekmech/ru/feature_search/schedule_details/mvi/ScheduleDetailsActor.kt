package kekmech.ru.feature_search.schedule_details.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository

internal class ScheduleDetailsActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<ScheduleDetailsAction, ScheduleDetailsEvent> {

    override fun execute(action: ScheduleDetailsAction): Observable<ScheduleDetailsEvent> = when (action) {
        is ScheduleDetailsAction.LoadSchedule -> scheduleRepository
            .loadSchedule(action.ownerName, action.weekOffset)
            .mapSuccessEvent { ScheduleDetailsEvent.News.ScheduleLoaded(it, action.weekOffset) }
    }
}