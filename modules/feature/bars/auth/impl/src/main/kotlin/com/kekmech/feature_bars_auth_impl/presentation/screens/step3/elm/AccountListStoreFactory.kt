package com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListState as State

internal typealias AccountListStore = Store<Event, Effect, State>

internal class AccountListStoreFactory(
    private val actor: AccountListActor,
) {

    fun create(): AccountListStore =
        ElmStore(
            initialState = State,
            reducer = AccountListReducer(),
            actor = actor,
            startEvent = Event.Ui.Init,
        )
}