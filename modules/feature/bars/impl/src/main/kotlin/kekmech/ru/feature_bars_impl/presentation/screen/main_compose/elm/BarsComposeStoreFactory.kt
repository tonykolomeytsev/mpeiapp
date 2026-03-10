package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store

internal typealias BarsComposeStore = Store<BarsComposeEvent, BarsComposeEffect, BarsComposeState>

internal class BarsComposeStoreFactory(
    private val actor: BarsComposeActor,
) {

    fun create(): BarsComposeStore = ElmStore(
        initialState = BarsComposeState(),
        reducer = BarsComposeReducer(),
        actor = actor,
        startEvent = BarsComposeEvent.Ui.Init,
    )
}