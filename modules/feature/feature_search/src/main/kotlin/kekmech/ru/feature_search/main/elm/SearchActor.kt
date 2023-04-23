package kekmech.ru.feature_search.main.elm

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.dto.SearchResultType.GROUP
import kekmech.ru.domain_schedule.dto.SearchResultType.PERSON
import kekmech.ru.feature_search.main.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search.main.utils.FullTextNotesSearchHelper
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_search.main.elm.SearchCommand as Command
import kekmech.ru.feature_search.main.elm.SearchEvent as Event

internal class SearchActor(
    private val notesRepository: NotesRepository,
    private val mapRepository: MapRepository,
    private val scheduleRepository: ScheduleRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.SearchNotes -> notesRepository.getNotes()
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
            is Command.SearchGroups -> scheduleRepository.getSearchResults(command.query, GROUP)
                .mapSuccessEvent { Event.Internal.SearchGroupsSuccess(it.items) }
            is Command.SearchPersons -> scheduleRepository.getSearchResults(command.query, PERSON)
                .mapSuccessEvent { Event.Internal.SearchPersonsSuccess(it.items) }
        }
}
