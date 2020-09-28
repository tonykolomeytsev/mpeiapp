package kekmech.ru.notes.note_list.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

typealias NoteListFeature = Feature<NoteListState, NoteListEvent, NoteListEffect>

data class NoteListState(
    val selectedClasses: Classes,
    val selectedDate: LocalDate,
    val notes: List<Note> = emptyList()
)

sealed class NoteListEvent {

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

sealed class NoteListEffect {
    object ShowNoteLoadError : NoteListEffect()
    data class OpenNoteEdit(val note: Note) : NoteListEffect()
}

sealed class NoteListAction {
    data class LoadNotesForClasses(val classes: Classes) : NoteListAction()
    data class DeleteNote(val note: Note) : NoteListAction()
}
