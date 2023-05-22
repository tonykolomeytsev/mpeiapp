package kekmech.ru.feature_bars.screen.main.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class BarsFeatureFactory(
    private val actor: BarsActor,
) {

    fun create() = ElmStoreCompat(
        initialState = BarsState(),
        reducer = BarsReducer(),
        actor = actor,
        startEvent = BarsEvent.Ui.Init,
    )
}
