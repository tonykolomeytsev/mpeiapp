package kekmech.ru.notes.edit.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import vivid.money.elmslie.core.store.Actor

internal class NoteEditActor(
    private val notesRepository: NotesRepository,
) : Actor<NoteEditAction, NoteEditEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: NoteEditAction): Observable<NoteEditEvent> = when (action) {
        is NoteEditAction.SaveNote -> notesRepository.putNote(action.note)
            .mapEvents(NoteEditEvent.News.NoteSavedSuccessfully, NoteEditEvent.News::NoteSaveError)
    }
}
