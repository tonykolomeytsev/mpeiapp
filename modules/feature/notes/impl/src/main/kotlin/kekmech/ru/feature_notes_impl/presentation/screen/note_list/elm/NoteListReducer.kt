package kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm

import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEvent.Internal
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEvent.Ui
import kekmech.ru.feature_schedule_api.domain.model.Classes
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import java.time.LocalDate
import java.time.LocalDateTime
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListCommand as Command
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEffect as Effect
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEvent as Event
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListState as State

internal class NoteListReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.LoadNotesForClassesSuccess -> state {
                copy(
                    notes = event.notes
                        .filter { note ->
                            matchesPredicate(
                                note = note,
                                classes = state.selectedClasses,
                                date = state.selectedDate,
                            )
                        }
                )
            }
            is Internal.LoadNotesForClassesFailure -> effects { +Effect.ShowNoteLoadError }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> commands { +Command.LoadNotesForClasses(state.selectedClasses) }
            is Ui.Click.CreateNewNote -> effects {
                +Effect.OpenNoteEdit(
                    note = Note(
                        content = "",
                        dateTime = LocalDateTime.of(
                            state.selectedDate,
                            state.selectedClasses.time.start
                        ),
                        classesName = state.selectedClasses.name,
                        target = 0,
                    )
                )
            }
            is Ui.Click.EditNote -> effects { +Effect.OpenNoteEdit(event.note) }
            is Ui.Action.DeleteNote -> {
                state { copy(notes = state.notes.filter { it != event.note }) }
                commands { +Command.DeleteNote(event.note) }
            }
        }

    @Suppress("ReturnCount")
    private fun matchesPredicate(note: Note, classes: Classes, date: LocalDate): Boolean {
        if (note.classesName != classes.name) return false
        if (note.dateTime.toLocalDate() != date) return false
        if (note.dateTime.toLocalTime() != classes.time.start) return false
        return true
    }
}
