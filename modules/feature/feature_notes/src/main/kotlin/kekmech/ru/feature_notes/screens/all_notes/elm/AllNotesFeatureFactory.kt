package kekmech.ru.feature_notes.screens.all_notes.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class AllNotesFeatureFactory(
    private val actor: AllNotesActor,
) {

    fun create() = ElmStoreCompat(
        initialState = AllNotesState(),
        reducer = AllNotesReducer(),
        actor = actor,
        startEvent = AllNotesEvent.Ui.Init,
    )
}
