package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

internal data object LoginPasswordState

internal sealed interface LoginPasswordEvent {
    sealed interface Ui : LoginPasswordEvent {
        data object Init : Ui
    }

    sealed interface Internal : LoginPasswordEvent
}

internal sealed interface LoginPasswordCommand

internal sealed interface LoginPasswordEffect
