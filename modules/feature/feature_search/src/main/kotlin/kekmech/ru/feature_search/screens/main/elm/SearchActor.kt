package kekmech.ru.feature_search.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.feature_search.screens.main.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search.screens.main.utils.FullTextNotesSearchHelper
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_search.screens.main.elm.SearchCommand as Command
import kekmech.ru.feature_search.screens.main.elm.SearchEvent as Event

internal class SearchActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val mapRepository: MapRepository,
    private val scheduleSearchRepository: ScheduleSearchRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.SearchNotes -> getNotesUseCase.getNotes()
                .flatMap { notes ->
                    Single.fromCallable {
                        FullTextNotesSearchHelper(
                            notes = notes,
                            query = command.query
                        ).execute()
                    }
                }
                .mapSuccessEvent(Event.Internal::SearchNotesSuccess)
            is Command.SearchMap -> mapRepository.getMarkers()
                .flatMap { mapMarkers ->
                    Single.fromCallable {
                        FullTextMapMarkersSearchHelper(
                            mapMarkers = mapMarkers,
                            query = command.query
                        ).execute()
                    }
                }
                .mapSuccessEvent(Event.Internal::SearchMapSuccess)
            is Command.SearchGroups -> scheduleSearchRepository
                .getSearchResults(
                    query = command.query,
                    type = ScheduleType.GROUP,
                )
                .mapSuccessEvent { Event.Internal.SearchGroupsSuccess(it.items) }
            is Command.SearchPersons -> scheduleSearchRepository
                .getSearchResults(
                    query = command.query,
                    type = ScheduleType.PERSON,
                )
                .mapSuccessEvent { Event.Internal.SearchPersonsSuccess(it.items) }
        }
}
