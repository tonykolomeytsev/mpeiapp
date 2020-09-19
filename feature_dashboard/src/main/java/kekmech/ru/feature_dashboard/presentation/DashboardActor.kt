package kekmech.ru.feature_dashboard.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News

class DashboardActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadTodaySchedule -> scheduleRepository.loadSchedule()
            .flatMapObservable { Observable.fromIterable(it.weeks.first().days) }
            .filter { it.dayOfWeek == 3 /*LocalDate.now().dayOfWeek.value*/ }
            .firstOrError()
            .mapEvents({ day -> News.TodayScheduleLoaded(day.classes) }, News::TodayScheduleLoadError)

    }
}