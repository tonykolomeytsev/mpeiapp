package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm

import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleCommand as Command

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
                searchResult.first {
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
                    results = it.take(
                        MaxSearchResultsNumber
                    ),
                )
            })
        }
}
