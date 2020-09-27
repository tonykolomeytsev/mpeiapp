package kekmech.ru.notes.edit.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_notes.NotesRepository

class NoteEditActor(
    private val notesRepository: NotesRepository
) : Actor<NoteEditAction, NoteEditEvent> {

    override fun execute(action: NoteEditAction): Observable<NoteEditEvent> = when (action) {
        is NoteEditAction.SaveNote -> notesRepository.putNote(action.note)
            .mapEvents(NoteEditEvent.News.NoteSavedSuccessfully, NoteEditEvent.News::NoteSaveError)
    }
}