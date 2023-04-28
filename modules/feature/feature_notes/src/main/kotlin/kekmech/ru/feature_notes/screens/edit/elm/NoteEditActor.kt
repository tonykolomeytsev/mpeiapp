package kekmech.ru.feature_notes.screens.edit.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditCommand as Command
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditEvent as Event

internal class NoteEditActor(
    private val putNoteUseCase: PutNoteForSelectedScheduleUseCase,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.SaveNote -> putNoteUseCase.putNote(command.note)
                .mapEvents(
                    successEvent = Internal.SaveNoteSuccess,
                    failureEventMapper = Internal::SaveNoteFailure,
                )
        }
}
