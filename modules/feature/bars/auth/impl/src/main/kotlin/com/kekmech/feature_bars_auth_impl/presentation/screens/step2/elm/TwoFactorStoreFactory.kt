package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorState as State

internal typealias TwoFactorStore = Store<Event, Effect, State>

internal class TwoFactorStoreFactory(
    private val actor: TwoFactorActor,
) {

    fun create(): TwoFactorStore =
        ElmStore(
            initialState = State(),
            reducer = TwoFactorReducer(),
            actor = actor,
            startEvent = Event.Ui.Init,
        )
}