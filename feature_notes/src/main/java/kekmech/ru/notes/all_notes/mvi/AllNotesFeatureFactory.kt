package kekmech.ru.notes.all_notes.mvi

import kekmech.ru.common_mvi.BaseFeature

class AllNotesFeatureFactory(
    private val actor: AllNotesActor
) {

    fun create() = BaseFeature(
        initialState = AllNotesState(),
        reducer = AllNotesReducer(),
        actor = actor
    ).start()
}