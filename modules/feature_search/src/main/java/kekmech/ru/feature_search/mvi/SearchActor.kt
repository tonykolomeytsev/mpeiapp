package kekmech.ru.feature_search.mvi

import io.reactivex.Observable
import io.reactivex.Single
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.dto.SearchResultType
import kekmech.ru.domain_schedule.dto.SearchResultType.GROUP
import kekmech.ru.domain_schedule.dto.SearchResultType.PERSON
import kekmech.ru.feature_search.utils.FullTextMapMarkersSearchHelper
import kekmech.ru.feature_search.utils.FullTextNotesSearchHelper

internal class SearchActor(
    private val notesRepository: NotesRepository,
    private val mapRepository: MapRepository,
    private val scheduleRepository: ScheduleRepository
) : Actor<SearchAction, SearchEvent> {

    override fun execute(action: SearchAction): Observable<SearchEvent> = when (action) {
        is SearchAction.SearchNotes -> notesRepository.getNotes()
            .flatMap { Single.just(FullTextNotesSearchHelper(it, action.query).execute()) }
            .mapSuccessEvent(SearchEvent.News::SearchNotesSuccess)
        is SearchAction.SearchMap -> mapRepository.observeMarkers()
            .flatMap { Single.just(FullTextMapMarkersSearchHelper(it.markers, action.query).execute()) }
            .mapSuccessEvent(SearchEvent.News::SearchMapSuccess)
        is SearchAction.SearchGroups -> scheduleRepository.getSearchResults(action.query, GROUP)
            .mapSuccessEvent { SearchEvent.News.SearchGroupsSuccess(it.items) }
        is SearchAction.SearchPersons -> scheduleRepository.getSearchResults(action.query, PERSON)
            .mapSuccessEvent { SearchEvent.News.SearchPersonsSuccess(it.items) }
    }
}