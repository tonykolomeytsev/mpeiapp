package kekmech.ru.feature_dashboard.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News

class DashboardActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadSchedule -> scheduleRepository.loadSchedule()
            .mapEvents({ News.ScheduleLoaded(it, action.weekOffset) }, News::ScheduleLoadError)

    }
}