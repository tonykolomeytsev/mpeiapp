package kekmech.ru.bars.screen.main.elm

import vivid.money.elmslie.core.store.ElmStore

internal class BarsFeatureFactory(
    private val actor: BarsActor
) {

    fun create() = ElmStore(
        initialState = BarsState(),
        reducer = BarsReducer(),
        actor = actor
    )
}
