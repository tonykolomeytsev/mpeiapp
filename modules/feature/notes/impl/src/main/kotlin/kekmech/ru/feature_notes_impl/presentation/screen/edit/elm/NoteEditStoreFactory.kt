package kekmech.ru.feature_notes_impl.presentation.screen.edit.elm

import kekmech.ru.feature_notes_api.domain.model.Note
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class NoteEditStoreFactory(
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
