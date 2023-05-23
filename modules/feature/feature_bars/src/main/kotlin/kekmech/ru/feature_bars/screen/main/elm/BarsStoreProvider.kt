package kekmech.ru.feature_bars.screen.main.elm

import kekmech.ru.common_kotlin.fastLazy
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal typealias BarsStore = Store<BarsEvent, BarsEffect, BarsState>


internal class BarsStoreProvider(
    private val actor: BarsActor,
) {

    private val store by fastLazy {
        ElmStoreCompat(
            initialState = BarsState(),
            reducer = BarsReducer(),
            actor = actor,
            startEvent = BarsEvent.Ui.Init,
        )
    }

    fun get(): BarsStore = store
}
