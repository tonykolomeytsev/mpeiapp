package kekmech.ru.feature_notes.screens.edit.elm

import kekmech.ru.domain_notes.dto.Note
import vivid.money.elmslie.core.store.ElmStore

internal class NoteEditFeatureFactory(
    private val actor: NoteEditActor,
) {

    fun create(
        note: Note,
    ) = ElmStore(
        initialState = NoteEditState(note),
        reducer = NoteEditReducer(),
        actor = actor,
    )
}
