package kekmech.ru.feature_notes.screens.note_list.elm

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

internal data class NoteListState(
    val selectedClasses: Classes,
    val selectedDate: LocalDate,
    val notes: List<Note> = emptyList(),
)

internal sealed interface NoteListEvent {

    sealed interface Ui : NoteListEvent {
        object Init : Ui

        object Click {
            object CreateNewNote : Ui
            data class EditNote(val note: Note) : Ui
        }

        object Action {
            data class DeleteNote(val note: Note) : Ui
        }
    }

    sealed interface Internal : NoteListEvent {
        data class LoadNotesForClassesSuccess(val notes: List<Note>) : Internal
        data class LoadNotesForClassesFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface NoteListEffect {
    object ShowNoteLoadError : NoteListEffect
    data class OpenNoteEdit(val note: Note) : NoteListEffect
}

internal sealed interface NoteListCommand {
    data class LoadNotesForClasses(val classes: Classes) : NoteListCommand
    data class DeleteNote(val note: Note) : NoteListCommand
}
