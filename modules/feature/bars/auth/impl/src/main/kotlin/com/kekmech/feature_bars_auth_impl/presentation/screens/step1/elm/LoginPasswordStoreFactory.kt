package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordState as State

internal typealias LoginPasswordStore = Store<Event, Effect, State>

internal class LoginPasswordStoreFactory(
    private val actor: LoginPasswordActor,
) {

    fun create(): LoginPasswordStore =
        ElmStore(
            initialState = State(),
            reducer = LoginPasswordReducer(),
            actor = actor,
            startEvent = Event.Ui.Init,
        )
}