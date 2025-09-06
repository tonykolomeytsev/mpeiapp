package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.fastLazy
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store

internal typealias BarsStore = Store<BarsEvent, BarsEffect, BarsState>


internal class BarsStoreProvider(
    private val actor: BarsActor,
) {

    private val store by fastLazy {
        ElmStore(
            initialState = BarsState(),
            reducer = BarsReducer(),
            actor = actor,
            startEvent = BarsEvent.Ui.Init,
        )
    }

    fun get(): BarsStore = store
}
