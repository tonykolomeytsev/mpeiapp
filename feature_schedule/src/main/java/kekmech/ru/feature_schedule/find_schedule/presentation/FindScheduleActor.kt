package kekmech.ru.feature_schedule.find_schedule.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleEvent.News

class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<FindScheduleAction, FindScheduleEvent> {

    override fun execute(action: FindScheduleAction): Observable<FindScheduleEvent> = when (action) {
        is FindScheduleAction.FindGroup -> scheduleRepository
            .loadSchedule(action.groupName)
            .mapEvents(News.GroupLoadedSuccessfully(action.groupName), News::GroupLoadingError)
        is FindScheduleAction.SelectGroup -> scheduleRepository
            .selectGroup(action.groupName)
            .toObservable()
    }
}