package kekmech.ru.feature_notes.edit.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.feature_notes.edit.elm.NoteEditEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_notes.edit.elm.NoteEditCommand as Command
import kekmech.ru.feature_notes.edit.elm.NoteEditEvent as Event

internal class NoteEditActor(
    private val notesRepository: NotesRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.SaveNote -> notesRepository.putNote(command.note)
                .mapEvents(
                    successEvent = Internal.SaveNoteSuccess,
                    failureEventMapper = Internal::SaveNoteFailure,
                )
        }
}
