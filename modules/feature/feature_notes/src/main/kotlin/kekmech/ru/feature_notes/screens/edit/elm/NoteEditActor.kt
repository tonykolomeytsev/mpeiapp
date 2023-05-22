package kekmech.ru.feature_notes.screens.edit.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.rxCompletable
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditCommand as Command
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditEvent as Event

internal class NoteEditActor(
    private val putNoteUseCase: PutNoteForSelectedScheduleUseCase,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SaveNote -> actorFlow { putNoteUseCase.invoke(command.note) }
                .mapEvents(
                    eventMapper = { Internal.SaveNoteSuccess },
                    errorMapper = Internal::SaveNoteFailure,
                )
        }
}
