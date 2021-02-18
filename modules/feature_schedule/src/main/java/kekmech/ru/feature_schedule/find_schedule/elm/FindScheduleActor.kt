package kekmech.ru.feature_schedule.find_schedule.elm

import io.reactivex.Observable
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.News
import vivid.money.elmslie.core.store.Actor

private const val MAX_SEARCH_RESULTS_COUNT = 10

internal class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository
) : Actor<FindScheduleAction, FindScheduleEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: FindScheduleAction): Observable<FindScheduleEvent> = when (action) {
        is FindScheduleAction.FindGroup -> scheduleRepository
            .loadSchedule(action.scheduleName)
            .mapEvents(News.GroupLoadedSuccessfully(action.scheduleName), News::GroupLoadingError)
        is FindScheduleAction.SelectGroup -> scheduleRepository
            .selectSchedule(action.scheduleName)
            .toObservable()
        is FindScheduleAction.SearchForAutocomplete -> scheduleRepository
            .getSearchResults(action.query)
            .mapSuccessEvent(successEventMapper = { News.SearchResultsLoaded(it.items.take(MAX_SEARCH_RESULTS_COUNT)) })
    }
}