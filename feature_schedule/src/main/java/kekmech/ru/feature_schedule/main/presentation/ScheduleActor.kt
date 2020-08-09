package kekmech.ru.feature_schedule.main.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class ScheduleActor : Actor<ScheduleAction, ScheduleEvent> {

    override fun execute(action: ScheduleAction): Observable<ScheduleEvent> {
        TODO("Not yet implemented")
    }
}