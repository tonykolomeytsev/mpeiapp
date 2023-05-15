package kekmech.ru.feature_notes.screens.all_notes.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxCompletable
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesCommand as Command
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesEvent as Event

internal class AllNotesActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val notesRepository: NotesRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadAllNotes -> rxSingle(Dispatchers.Unconfined) { getNotesUseCase.invoke() }
                .mapEvents(
                    successEventMapper = Internal::LoadAllNotesSuccess,
                    failureEventMapper = Internal::LoadAllNotesFailure,
                )
            is Command.DeleteNote -> rxCompletable(Dispatchers.Unconfined) {
                notesRepository.deleteNote(command.note)
            }.toObservable()
        }
}
