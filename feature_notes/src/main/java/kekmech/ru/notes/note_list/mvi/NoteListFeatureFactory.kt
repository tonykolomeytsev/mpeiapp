package kekmech.ru.notes.note_list.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

class NoteListFeatureFactory(
    private val actor: NoteListActor
) {

    fun create(selectedClasses: Classes, selectedDate: LocalDate) = BaseFeature(
        initialState = NoteListState(selectedClasses, selectedDate),
        reducer = NoteListReducer(),
        actor = actor
    ).start()
}