package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainState as State

internal typealias BarsAuthMainStore = Store<Event, Effect, State>

internal class BarsAuthMainStoreFactory(
    private val actor: BarsAuthMainActor,
) {

    fun create(): BarsAuthMainStore =
        ElmStore(
            initialState = State(),
            reducer = BarsAuthMainReducer(),
            actor = actor,
            startEvent = Event.Ui.Init,
        )
}