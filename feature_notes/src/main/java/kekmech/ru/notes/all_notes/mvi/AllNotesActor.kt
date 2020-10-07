package kekmech.ru.notes.all_notes.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_notes.NotesRepository

internal class AllNotesActor(
    private val notesRepository: NotesRepository
) : Actor<AllNotesAction, AllNotesEvent> {

    override fun execute(action: AllNotesAction): Observable<AllNotesEvent> = when (action) {
        is AllNotesAction.LoadAllNotes -> notesRepository.getNotes()
            .mapEvents(AllNotesEvent.News::NotesSuccessfullyLoaded, AllNotesEvent.News::NotesLoadError)
        is AllNotesAction.DeleteNote -> notesRepository.deleteNote(action.note)
            .toObservable()
    }
}