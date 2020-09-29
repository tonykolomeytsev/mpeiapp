package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_notes.dto.Note

typealias NoteEditFeature = Feature<NoteEditState, NoteEditEvent, NoteEditEffect>

data class NoteEditState(
    val note: Note
)

sealed class NoteEditEvent {

    sealed class Wish : NoteEditEvent() {
        object Init : Wish()

        object Click {
            object SaveNote : Wish()
        }

        object Action {
            data class NoteContentChanged(val content: String) : Wish()
        }
    }

    sealed class News : NoteEditEvent() {
        object NoteSavedSuccessfully : News()
        data class NoteSaveError(val throwable: Throwable) : News()
    }
}

sealed class NoteEditEffect {
    object CloseWithSuccess : NoteEditEffect()
    object ShowError : NoteEditEffect()
}

sealed class NoteEditAction {
    data class SaveNote(val note: Note) : NoteEditAction()
}