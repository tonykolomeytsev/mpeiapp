package kekmech.ru.feature_search.screens.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.feature_search.screens.main.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search.screens.main.utils.FullTextNotesSearchHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_search.screens.main.elm.SearchCommand as Command
import kekmech.ru.feature_search.screens.main.elm.SearchEvent as Event

internal class SearchActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val mapRepository: MapRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SearchNotes -> actorFlow {
                val notes = getNotesUseCase.invoke()
                FullTextNotesSearchHelper(notes = notes, query = command.query).execute()
            }.mapEvents(Event.Internal::SearchNotesSuccess)

            is Command.SearchMap -> actorFlow {
                val mapMarkers = mapRepository.getMarkers().await()
                FullTextMapMarkersSearchHelper(
                    mapMarkers = mapMarkers,
                    query = command.query
                ).execute()
            }.mapEvents(Event.Internal::SearchMapSuccess)

            is Command.SearchGroups -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(
                        query = command.query,
                        type = ScheduleType.GROUP,
                    ).await()
            }.mapEvents({ Event.Internal.SearchGroupsSuccess(it.items) })

            is Command.SearchPersons -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(
                        query = command.query,
                        type = ScheduleType.PERSON,
                    ).await()
            }.mapEvents({ Event.Internal.SearchPersonsSuccess(it.items) })
        }
}
