package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store

internal typealias BarsStore = Store<BarsEvent, BarsEffect, BarsState>

internal class BarsStoreFactory(
    private val actor: BarsActor,
) {

    fun create(): BarsStore = ElmStore(
        initialState = BarsState(),
        reducer = BarsReducer(),
        actor = actor,
        startEvent = BarsEvent.Ui.Init,
    )
}
