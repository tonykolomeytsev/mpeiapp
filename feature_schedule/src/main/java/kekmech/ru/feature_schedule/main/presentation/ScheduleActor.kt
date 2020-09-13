package kekmech.ru.feature_schedule.main.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository

class ScheduleActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<ScheduleAction, ScheduleEvent> {

    override fun execute(action: ScheduleAction): Observable<ScheduleEvent> = when (action) {
        is ScheduleAction.ObserveSchedule -> scheduleRepository.observeSchedule(action.weekOffset)
    }
}