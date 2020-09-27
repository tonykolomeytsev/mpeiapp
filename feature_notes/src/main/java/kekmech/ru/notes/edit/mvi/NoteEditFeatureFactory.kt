package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes

class NoteEditFeatureFactory(
    private val actor: NoteEditActor
) {

    fun create(
        note: Note,
        classes: Classes
    ) = BaseFeature(
        initialState = NoteEditState(note, classes),
        reducer = NoteEditReducer(),
        actor = actor
    )
}