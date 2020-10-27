package kekmech.ru.feature_search.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_notes.NotesRepository

internal class SearchActor(
    private val notesRepository: NotesRepository,
    private val mapRepository: MapRepository
) : Actor<SearchAction, SearchEvent> {

    override fun execute(action: SearchAction): Observable<SearchEvent> = TODO()
}