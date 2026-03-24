package com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm

import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListState as State

internal class AccountListReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal) {
        TODO("Not yet implemented")
    }

    override fun Result.ui(event: Ui) {
        TODO("Not yet implemented")
    }
}