package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes

typealias NoteEditFeature = Feature<NoteEditState, NoteEditEvent, NoteEditEffect>

data class NoteEditState(
    val note: Note,
    val classes: Classes
)

sealed class NoteEditEvent {

    sealed class Wish : NoteEditEvent() {
        object Init : Wish()
    }

    sealed class News : NoteEditEvent() {
        object NoteSavedSuccessfully : News()
        data class NoteSaveError(val throwable: Throwable) : News()
    }
}

sealed class NoteEditEffect {
    object CloseWithSuccess : NoteEditEffect()
}

sealed class NoteEditAction {
    data class SaveNote(val note: Note) : NoteEditAction()
}