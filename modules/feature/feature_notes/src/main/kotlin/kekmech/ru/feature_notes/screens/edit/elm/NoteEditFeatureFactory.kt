package kekmech.ru.feature_notes.screens.edit.elm

import kekmech.ru.domain_notes.dto.Note
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class NoteEditFeatureFactory(
    private val actor: NoteEditActor,
) {

    fun create(
        note: Note,
    ) = ElmStoreCompat(
        initialState = NoteEditState(note),
        reducer = NoteEditReducer(),
        actor = actor,
        startEvent = NoteEditEvent.Ui.Init,
    )
}
