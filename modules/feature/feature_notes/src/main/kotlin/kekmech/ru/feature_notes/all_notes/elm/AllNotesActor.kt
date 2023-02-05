package kekmech.ru.feature_notes.all_notes.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import vivid.money.elmslie.core.store.Actor

internal class AllNotesActor(
    private val notesRepository: NotesRepository
) : Actor<AllNotesAction, AllNotesEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: AllNotesAction): Observable<AllNotesEvent> = when (action) {
        is AllNotesAction.LoadAllNotes -> notesRepository.getNotes()
            .mapEvents(AllNotesEvent.News::NotesSuccessfullyLoaded, AllNotesEvent.News::NotesLoadError)
        is AllNotesAction.DeleteNote -> notesRepository.deleteNote(action.note)
            .toObservable()
    }
}
