package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import com.kekmech.feature_bars_auth_impl.domain.LoginPasswordError
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

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.LoginWithPasswordSuccess -> Unit
        is Internal.LoginWithPasswordFailure -> when (event.loginPasswordError) {
            is LoginPasswordError.InvalidCredentials -> {
                state { copy(isLoading = false) }
                effects { +Effect.ShowInvalidCredentialsAlert }
            }

            is LoginPasswordError.Network -> {
                state { copy(isLoading = false) }
                effects { +Effect.ShowNetworkErrorBanner }
            }

            is LoginPasswordError.Fatal -> {
                state { copy(isLoading = false) }
            }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> Unit
        is Ui.Action.SubmitLoginPassword -> {
            state {
                copy(
                    login = event.login,
                    password = event.password,
                    isLoading = true,
                )
            }
            commands { +Command.LoginWithPassword(event.login, event.password) }
        }
    }
}