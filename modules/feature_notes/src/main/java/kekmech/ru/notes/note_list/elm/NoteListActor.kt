package kekmech.ru.notes.note_list.elm

import io.reactivex.Observable
import kekmech.ru.domain_notes.NotesRepository
import vivid.money.elmslie.core.store.Actor

internal class NoteListActor(
    private val notesRepository: NotesRepository
) : Actor<NoteListAction, NoteListEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: NoteListAction): Observable<NoteListEvent> = when (action) {
        is NoteListAction.LoadNotesForClasses -> notesRepository.getNotes()
            .mapEvents(NoteListEvent.News::NotesLoaded, NoteListEvent.News::NotesLoadError)
        is NoteListAction.DeleteNote -> notesRepository.deleteNote(action.note)
            .toObservable() // todo error handling
    }
}