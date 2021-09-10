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

internal class SearchActor(
    private val notesRepository: NotesRepository,
    private val mapRepository: MapRepository,
    private val scheduleRepository: ScheduleRepository
) : Actor<SearchAction, SearchEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: SearchAction): Observable<SearchEvent> = when (action) {
        is SearchAction.SearchNotes -> notesRepository.getNotes()
            .flatMap { Single.just(FullTextNotesSearchHelper(it, action.query).execute()) }
            .mapSuccessEvent(SearchEvent.News::SearchNotesSuccess)
        is SearchAction.SearchMap -> mapRepository.getMarkers()
            .flatMap { Single.just(FullTextMapMarkersSearchHelper(it.markers, action.query).execute()) }
            .mapSuccessEvent(SearchEvent.News::SearchMapSuccess)
        is SearchAction.SearchGroups -> scheduleRepository.getSearchResults(action.query, GROUP)
            .mapSuccessEvent { SearchEvent.News.SearchGroupsSuccess(it.items) }
        is SearchAction.SearchPersons -> scheduleRepository.getSearchResults(action.query, PERSON)
            .mapSuccessEvent { SearchEvent.News.SearchPersonsSuccess(it.items) }
    }
}