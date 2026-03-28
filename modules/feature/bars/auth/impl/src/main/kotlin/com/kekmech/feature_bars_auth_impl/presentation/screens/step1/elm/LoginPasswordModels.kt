package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import com.kekmech.feature_bars_auth_impl.domain.LoginPasswordError

internal data class LoginPasswordState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

internal sealed interface LoginPasswordEvent {
    sealed interface Ui : LoginPasswordEvent {
        data object Init : Ui

        sealed interface Action : Ui {
            data class SubmitLoginPassword(val login: String, val password: String) : Action
        }
    }

    sealed interface Internal : LoginPasswordEvent {
        data object LoginWithPasswordSuccess : Internal
        data class LoginWithPasswordFailure(
            val loginPasswordError: LoginPasswordError,
        ) : Internal
    }
}

internal sealed interface LoginPasswordCommand {

    data class LoginWithPassword(
        val login: String,
        val password: String,
    ) : LoginPasswordCommand
}

internal sealed interface LoginPasswordEffect {
    data object ShowInvalidCredentialsAlert : LoginPasswordEffect
    data object ShowNetworkErrorBanner : LoginPasswordEffect
    data object ShowFatalErrorBanner : LoginPasswordEffect
}
