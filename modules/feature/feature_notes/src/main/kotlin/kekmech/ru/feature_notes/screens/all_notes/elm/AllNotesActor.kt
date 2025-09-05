package kekmech.ru.feature_notes.screens.all_notes.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesCommand as Command
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesEvent as Event

internal class AllNotesActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val notesRepository: NotesRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadAllNotes -> actorFlow {
                getNotesUseCase.getNotes().await()
            }.mapEvents(Internal::LoadAllNotesSuccess, Internal::LoadAllNotesFailure)

            is Command.DeleteNote -> actorEmptyFlow {
                notesRepository.deleteNote(command.note)
            }
        }
}
