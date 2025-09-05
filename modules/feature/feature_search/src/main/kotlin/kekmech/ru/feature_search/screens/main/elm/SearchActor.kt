package kekmech.ru.feature_search.screens.main.elm

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.feature_search.screens.main.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search.screens.main.utils.FullTextNotesSearchHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_search.screens.main.elm.SearchCommand as Command
import kekmech.ru.feature_search.screens.main.elm.SearchEvent as Event

internal class SearchActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val mapRepository: MapRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SearchNotes -> actorFlow {
                getNotesUseCase.getNotes()
                    .flatMap { notes ->
                        Single.fromCallable {
                            FullTextNotesSearchHelper(
                                notes = notes,
                                query = command.query
                            ).execute()
                        }
                    }.await()
            }.mapEvents(Event.Internal::SearchNotesSuccess)

            is Command.SearchMap -> actorFlow {
                mapRepository.getMarkers()
                    .flatMap { mapMarkers ->
                        Single.fromCallable {
                            FullTextMapMarkersSearchHelper(
                                mapMarkers = mapMarkers,
                                query = command.query
                            ).execute()
                        }
                    }
                    .await()
            }.mapEvents(Event.Internal::SearchMapSuccess)

            is Command.SearchGroups -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(
                        query = command.query,
                        type = ScheduleType.GROUP,
                    )
                    .await()
                    .items
            }.mapEvents(Event.Internal::SearchGroupsSuccess)

            is Command.SearchPersons -> actorFlow {
                scheduleSearchRepository
                    .getSearchResults(
                        query = command.query,
                        type = ScheduleType.PERSON,
                    )
                    .await()
                    .items
            }.mapEvents(Event.Internal::SearchPersonsSuccess)
        }
}
