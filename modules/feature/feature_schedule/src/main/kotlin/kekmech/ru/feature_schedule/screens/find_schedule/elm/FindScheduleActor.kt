package kekmech.ru.feature_schedule.screens.find_schedule.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleCommand as Command

private const val MaxSearchResultsNumber = 10

internal class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, FindScheduleEvent> {

    override fun execute(command: Command): Observable<FindScheduleEvent> =
        when (command) {
            is Command.FindSchedule -> scheduleSearchRepository
                .getSearchResults(query = command.name)
                .map {
                    it.items.first { searchResult ->
                        searchResult.name.equals(
                            other = command.name,
                            ignoreCase = true,
                        )
                    }
                }
                .mapEvents(
                    successEventMapper = {
                        Internal.FindScheduleSuccess(
                            name = it.name,
                            type = it.type,
                        )
                    },
                    failureEventMapper = Internal::FindScheduleFailure,
                )
            is Command.SelectSchedule -> scheduleRepository
                .setSelectedSchedule(command.selectedSchedule)
                .toObservable()
            is Command.SearchForAutocomplete -> scheduleSearchRepository
                .getSearchResults(command.query)
                .mapSuccessEvent(successEventMapper = {
                    Internal.SearchForAutocompleteSuccess(
                        results = it.items.take(MaxSearchResultsNumber),
                    )
                })
        }
}
