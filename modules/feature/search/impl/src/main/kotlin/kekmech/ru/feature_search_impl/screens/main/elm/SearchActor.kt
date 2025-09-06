package kekmech.ru.feature_search_impl.screens.main.elm

import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_notes_api.domain.usecase.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_search_impl.screens.main.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search_impl.screens.main.utils.FullTextNotesSearchHelper
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_search_impl.screens.main.elm.SearchCommand as Command
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent as Event

internal class SearchActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val mapRepository: MapRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SearchNotes -> actorFlow {
                val notes = getNotesUseCase.invoke()
                FullTextNotesSearchHelper(notes = notes, query = command.query).execute()
            }.mapEvents(Event.Internal::SearchNotesSuccess)

            is Command.SearchMap -> actorFlow {
                val mapMarkers = mapRepository.getMarkers().getOrThrow()
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
                    )
                    .getOrThrow()
            }.mapEvents({ Event.Internal.SearchGroupsSuccess(it) })

            is Command.SearchPersons -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(
                        query = command.query,
                        type = ScheduleType.PERSON,
                    )
                    .getOrThrow()
            }.mapEvents({ Event.Internal.SearchPersonsSuccess(it) })
        }
}
