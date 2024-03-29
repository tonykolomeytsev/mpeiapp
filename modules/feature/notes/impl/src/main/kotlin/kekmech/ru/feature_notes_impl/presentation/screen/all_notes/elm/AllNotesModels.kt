package kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm

import kekmech.ru.feature_notes_api.domain.model.Note

internal data class AllNotesState(
    val notes: List<Note>? = null,
)

internal sealed interface AllNotesEvent {

    sealed interface Ui : AllNotesEvent {

        object Init : Ui

        object Action {
            data class DeleteNote(val note: Note) : Ui
        }
    }

    sealed interface Internal : AllNotesEvent {
        data class LoadAllNotesSuccess(val notes: List<Note>) : Internal
        data class LoadAllNotesFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface AllNotesEffect {
    data class ShowError(val throwable: Throwable) : AllNotesEffect
}

internal sealed interface AllNotesCommand {
    object LoadAllNotes : AllNotesCommand
    data class DeleteNote(val note: Note) : AllNotesCommand
}
