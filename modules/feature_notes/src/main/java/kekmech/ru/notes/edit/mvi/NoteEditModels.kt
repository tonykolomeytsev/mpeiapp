package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_notes.dto.Note

internal typealias NoteEditFeature = Feature<NoteEditState, NoteEditEvent, NoteEditEffect>

internal data class NoteEditState(
    val note: Note
)

internal sealed class NoteEditEvent {

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

internal sealed class NoteEditEffect {
    object CloseWithSuccess : NoteEditEffect()
    object ShowError : NoteEditEffect()
}

internal sealed class NoteEditAction {
    data class SaveNote(val note: Note) : NoteEditAction()
}