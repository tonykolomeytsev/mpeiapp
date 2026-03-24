package com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm

internal data object AccountListState

internal sealed interface AccountListEvent {
    sealed interface Ui : AccountListEvent {
        data object Init : Ui
    }

    sealed interface Internal : AccountListEvent
}

internal sealed interface AccountListCommand

internal sealed interface AccountListEffect
