package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_notes.dto.Note

internal class NoteEditFeatureFactory(
    private val actor: NoteEditActor
) {

    fun create(
        note: Note
    ) = BaseFeature(
        initialState = NoteEditState(note),
        reducer = NoteEditReducer(),
        actor = actor
    ).start()
}