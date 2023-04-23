package kekmech.ru.feature_notes.edit.elm

import kekmech.ru.domain_notes.dto.Note

internal data class NoteEditState(
    val note: Note,
)

internal sealed interface NoteEditEvent {

    sealed interface Ui : NoteEditEvent {
        object Init : Ui

        object Click {
            object SaveNote : Ui
        }

        object Action {
            data class NoteContentChanged(val content: String) : Ui
        }
    }

    sealed interface Internal : NoteEditEvent {
        object SaveNoteSuccess : Internal
        data class SaveNoteFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface NoteEditEffect {
    object CloseWithSuccess : NoteEditEffect
    object ShowError : NoteEditEffect
}

internal sealed interface NoteEditCommand {
    data class SaveNote(val note: Note) : NoteEditCommand
}
