package kekmech.ru.feature_notes.screens.note_list.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListEvent.Internal
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListCommand as Command
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListEvent as Event

internal class NoteListActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val notesRepository: NotesRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadNotesForClasses -> actorFlow {
                getNotesUseCase.invoke()
            }.mapEvents(
                eventMapper = Internal::LoadNotesForClassesSuccess,
                errorMapper = Internal::LoadNotesForClassesFailure,
            )

            is Command.DeleteNote -> actorFlow {
                notesRepository.deleteNote(command.note)
            }.mapEvents() // todo: error handling
        }
}
