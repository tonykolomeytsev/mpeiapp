package kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm

import kekmech.ru.feature_schedule_api.domain.model.Classes
import money.vivid.elmslie.core.store.ElmStore
import java.time.LocalDate

internal class NoteListStoreFactory(
    private val actor: NoteListActor,
) {

    fun create(selectedClasses: Classes, selectedDate: LocalDate) = ElmStore(
        initialState = NoteListState(
            selectedClasses = selectedClasses,
            selectedDate = selectedDate,
        ),
        reducer = NoteListReducer(),
        actor = actor,
        startEvent = NoteListEvent.Ui.Init,
    )
}
