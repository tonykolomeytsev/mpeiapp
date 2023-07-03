package kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm

import kekmech.ru.feature_notes_api.domain.usecase.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes_impl.data.repository.NotesRepository
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Internal
import kekmech.ru.library_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesCommand as Command
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent as Event

internal class AllNotesActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val notesRepository: NotesRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadAllNotes -> actorFlow {
                getNotesUseCase.invoke()
            }.mapEvents(
                eventMapper = Internal::LoadAllNotesSuccess,
                errorMapper = Internal::LoadAllNotesFailure,
            )

            is Command.DeleteNote -> actorFlow {
                notesRepository.deleteNote(command.note)
            }.mapEvents()
        }
}
