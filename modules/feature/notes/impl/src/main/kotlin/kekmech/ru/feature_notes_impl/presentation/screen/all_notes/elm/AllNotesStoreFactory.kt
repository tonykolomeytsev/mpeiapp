package kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class AllNotesStoreFactory(
    private val actor: AllNotesActor,
) {

    fun create() = ElmStoreCompat(
        initialState = AllNotesState(),
        reducer = AllNotesReducer(),
        actor = actor,
        startEvent = AllNotesEvent.Ui.Init,
    )
}
