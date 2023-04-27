package kekmech.ru.feature_notes.screens.note_list.elm

import kekmech.ru.domain_schedule_models.dto.Classes
import vivid.money.elmslie.core.store.ElmStore
import java.time.LocalDate

internal class NoteListFeatureFactory(
    private val actor: NoteListActor,
) {

    fun create(selectedClasses: Classes, selectedDate: LocalDate) = ElmStore(
        initialState = NoteListState(
            selectedClasses = selectedClasses,
            selectedDate = selectedDate,
        ),
        reducer = NoteListReducer(),
        actor = actor,
    )
}
