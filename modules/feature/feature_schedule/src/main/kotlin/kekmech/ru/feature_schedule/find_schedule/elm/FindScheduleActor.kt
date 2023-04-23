package kekmech.ru.feature_schedule.find_schedule.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleCommand as Command

private const val MAX_SEARCH_RESULTS_COUNT = 10

internal class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository,
) : Actor<Command, FindScheduleEvent> {

    override fun execute(command: Command): Observable<FindScheduleEvent> =
        when (command) {
            is Command.FindGroup -> scheduleRepository
                .loadSchedule(command.scheduleName)
                .mapEvents(
                    successEvent = Internal.FindGroupSuccess(command.scheduleName),
                    failureEventMapper = Internal::FindGroupFailure,
                )
            is Command.SelectGroup -> scheduleRepository
                .selectSchedule(command.scheduleName)
                .toObservable()
            is Command.SearchForAutocomplete -> scheduleRepository
                .getSearchResults(command.query)
                .mapSuccessEvent(successEventMapper = {
                    Internal.SearchForAutocompleteSuccess(
                        results = it.items.take(MAX_SEARCH_RESULTS_COUNT),
                    )
                })
        }
}
