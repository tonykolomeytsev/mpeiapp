package kekmech.ru.feature_schedule.screens.find_schedule.elm

import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleCommand as Command

private const val MaxSearchResultsNumber = 10

internal class FindScheduleActor(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, FindScheduleEvent>() {

    override fun execute(command: Command): Flow<FindScheduleEvent> =
        when (command) {
            is Command.FindSchedule -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(query = command.name)
                    .map {
                        it.items.first { searchResult ->
                            searchResult.name.equals(
                                other = command.name,
                                ignoreCase = true,
                            )
                        }
                    }
                    .await()
            }
                .mapEvents(
                    eventMapper = {
                        Internal.FindScheduleSuccess(
                            name = it.name,
                            type = it.type,
                        )
                    },
                    errorMapper = Internal::FindScheduleFailure,
                )

            is Command.SelectSchedule -> actorEmptyFlow {
                scheduleRepository.setSelectedSchedule(command.selectedSchedule).await()
            }

            is Command.SearchForAutocomplete -> actorFlow {
                scheduleSearchRepository.getSearchResults(command.query).await()
            }.mapEvents(eventMapper = {
                Internal.SearchForAutocompleteSuccess(
                    results = it.items.take(MaxSearchResultsNumber),
                )
            })
        }
}
