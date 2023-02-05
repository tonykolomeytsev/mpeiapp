package kekmech.ru.feature_notes.all_notes.elm

import kekmech.ru.domain_notes.dto.Note

internal data class AllNotesState(
    val notes: List<Note>? = null
)

internal sealed class AllNotesEvent {

    sealed class Wish : AllNotesEvent() {

        object Init : Wish()

        object Action {
            data class DeleteNote(val note: Note) : Wish()
        }
    }

    sealed class News : AllNotesEvent() {
        data class NotesSuccessfullyLoaded(val notes: List<Note>) : News()
        data class NotesLoadError(val throwable: Throwable) : News()
    }
}

internal sealed class AllNotesEffect {
    data class ShowError(val throwable: Throwable) : AllNotesEffect()
}

internal sealed class AllNotesAction {
    object LoadAllNotes : AllNotesAction()
    data class DeleteNote(val note: Note) : AllNotesAction()
}
