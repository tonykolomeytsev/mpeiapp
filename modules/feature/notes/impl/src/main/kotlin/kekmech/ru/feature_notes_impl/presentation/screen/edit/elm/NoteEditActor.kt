package kekmech.ru.feature_notes_impl.presentation.screen.edit.elm

import kekmech.ru.feature_notes_api.domain.usecase.PutNoteForSelectedScheduleUseCase
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditCommand as Command
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent as Event

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
