package kekmech.ru.notes.note_list.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_notes.NotesRepository

class NoteListActor(
    private val notesRepository: NotesRepository
) : Actor<NoteListAction, NoteListEvent> {

    override fun execute(action: NoteListAction): Observable<NoteListEvent> = when (action) {
        is NoteListAction.LoadNotesForClasses -> notesRepository.getNotes()
            .mapEvents(NoteListEvent.News::NotesLoaded, NoteListEvent.News::NotesLoadError)
    }
}