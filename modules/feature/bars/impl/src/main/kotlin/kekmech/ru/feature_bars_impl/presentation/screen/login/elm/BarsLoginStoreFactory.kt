package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store

internal typealias BarsLoginStore = Store<BarsLoginEvent, BarsLoginEffect, BarsLoginState>

internal class BarsLoginStoreFactory(
    private val actor: BarsLoginActor,
) {

    fun create(): BarsLoginStore = ElmStore(
        initialState = BarsLoginState(),
        reducer = BarsLoginReducer(),
        actor = actor,
        startEvent = BarsLoginEvent.Ui.Init,
    )
}