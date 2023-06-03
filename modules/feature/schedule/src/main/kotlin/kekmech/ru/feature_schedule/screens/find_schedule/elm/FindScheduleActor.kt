package kekmech.ru.feature_schedule.screens.find_schedule.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.domain_schedule.data.ScheduleSearchRepository
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleCommand as Command

private const val MaxSearchResultsNumber = 10

internal class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, FindScheduleEvent> {

    override fun execute(command: Command): Flow<FindScheduleEvent> =
        when (command) {
            is Command.FindSchedule -> actorFlow {
                val searchResult = scheduleSearchRepository
                    .getSearchResults(query = command.name)
                    .getOrThrow()
                searchResult.items.first {
                    it.name.equals(
                        other = command.name,
                        ignoreCase = true,
                    )
                }
            }.mapEvents(
                eventMapper = {
                    Internal.FindScheduleSuccess(
                        name = it.name,
                        type = it.type,
                    )
                },
                errorMapper = Internal::FindScheduleFailure,
            )

            is Command.SelectSchedule -> actorFlow {
                scheduleRepository.setSelectedSchedule(command.selectedSchedule)
            }.mapEvents()

            is Command.SearchForAutocomplete -> actorFlow {
                scheduleSearchRepository.getSearchResults(command.query).getOrThrow()
            }.mapEvents({
                Internal.SearchForAutocompleteSuccess(
                    results = it.items.take(
                        MaxSearchResultsNumber
                    ),
                )
            })
        }
}
