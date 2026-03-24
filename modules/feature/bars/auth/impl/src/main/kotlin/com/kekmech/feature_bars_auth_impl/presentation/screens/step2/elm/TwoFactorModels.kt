package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

internal data object TwoFactorState

internal sealed interface TwoFactorEvent {
    sealed interface Ui : TwoFactorEvent {
        data object Init : Ui
    }

    sealed interface Internal : TwoFactorEvent
}

internal sealed interface TwoFactorCommand

internal sealed interface TwoFactorEffect
