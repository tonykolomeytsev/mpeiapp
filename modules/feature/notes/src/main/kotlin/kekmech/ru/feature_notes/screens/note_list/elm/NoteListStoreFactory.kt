package kekmech.ru.feature_notes.screens.note_list.elm

import kekmech.ru.domain_schedule_models.dto.Classes
import vivid.money.elmslie.coroutines.ElmStoreCompat
import java.time.LocalDate

internal class NoteListStoreFactory(
    private val actor: NoteListActor,
) {

    fun create(selectedClasses: Classes, selectedDate: LocalDate) = ElmStoreCompat(
        initialState = NoteListState(
            selectedClasses = selectedClasses,
            selectedDate = selectedDate,
        ),
        reducer = NoteListReducer(),
        actor = actor,
        startEvent = NoteListEvent.Ui.Init,
    )
}
