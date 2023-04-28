package kekmech.ru.feature_notes.screens.note_list.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListEvent.Internal
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListCommand as Command
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListEvent as Event

internal class NoteListActor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
    private val notesRepository: NotesRepository,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadNotesForClasses -> getNotesUseCase.getNotes()
                .mapEvents(
                    successEventMapper = Internal::LoadNotesForClassesSuccess,
                    failureEventMapper = Internal::LoadNotesForClassesFailure,
                )
            is Command.DeleteNote -> notesRepository.deleteNote(command.note)
                .toObservable() // todo: error handling
        }
}
