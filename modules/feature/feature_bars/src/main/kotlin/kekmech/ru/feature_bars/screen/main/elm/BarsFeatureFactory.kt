package kekmech.ru.feature_bars.screen.main.elm

import money.vivid.elmslie.core.store.ElmStore

internal class BarsFeatureFactory(
    private val actor: BarsActor,
) {

    fun create() = ElmStore(
        initialState = BarsState(),
        reducer = BarsReducer(),
        actor = actor,
    )
}
