package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorState as State

internal class TwoFactorReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
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