package kekmech.ru.feature_notes_impl.presentation.screen.edit.elm

import kekmech.ru.feature_notes_api.domain.model.Note
import money.vivid.elmslie.core.store.ElmStore

internal class NoteEditStoreFactory(
    private val actor: NoteEditActor,
) {

    fun create(
        note: Note,
    ) = ElmStore(
        initialState = NoteEditState(note),
        reducer = NoteEditReducer(),
        actor = actor,
        startEvent = NoteEditEvent.Ui.Init,
    )
}
