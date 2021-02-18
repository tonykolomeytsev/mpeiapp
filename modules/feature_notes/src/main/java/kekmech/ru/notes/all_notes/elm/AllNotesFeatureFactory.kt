package kekmech.ru.notes.all_notes.elm

import vivid.money.elmslie.core.store.ElmStore

internal class AllNotesFeatureFactory(
    private val actor: AllNotesActor
) {

    fun create() = ElmStore(
        initialState = AllNotesState(),
        reducer = AllNotesReducer(),
        actor = actor
    ).start()
}