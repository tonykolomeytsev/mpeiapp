package kekmech.ru.notes.note_list.elm

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

internal data class NoteListState(
    val selectedClasses: Classes,
    val selectedDate: LocalDate,
    val notes: List<Note> = emptyList(),
)

internal sealed class NoteListEvent {

    sealed class Wish : NoteListEvent() {
        object Init : Wish()

        object Click {
            object CreateNewNote : Wish()
            data class EditNote(val note: Note) : Wish()
        }

        object Action {
            data class DeleteNote(val note: Note) : Wish()
        }
    }

    sealed class News : NoteListEvent() {
        data class NotesLoaded(val notes: List<Note>) : News()
        data class NotesLoadError(val throwable: Throwable) : News()
    }
}

internal sealed class NoteListEffect {
    object ShowNoteLoadError : NoteListEffect()
    data class OpenNoteEdit(val note: Note) : NoteListEffect()
}

internal sealed class NoteListAction {
    data class LoadNotesForClasses(val classes: Classes) : NoteListAction()
    data class DeleteNote(val note: Note) : NoteListAction()
}
