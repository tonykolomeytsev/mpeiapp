package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordState as State

internal class LoginPasswordReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
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