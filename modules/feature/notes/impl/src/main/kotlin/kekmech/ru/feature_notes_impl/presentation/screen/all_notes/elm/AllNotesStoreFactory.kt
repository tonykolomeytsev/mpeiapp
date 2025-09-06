package kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm

import money.vivid.elmslie.core.store.ElmStore

internal class AllNotesStoreFactory(
    private val actor: AllNotesActor,
) {

    fun create() = ElmStore(
        initialState = AllNotesState(),
        reducer = AllNotesReducer(),
        actor = actor,
        startEvent = AllNotesEvent.Ui.Init,
    )
}
