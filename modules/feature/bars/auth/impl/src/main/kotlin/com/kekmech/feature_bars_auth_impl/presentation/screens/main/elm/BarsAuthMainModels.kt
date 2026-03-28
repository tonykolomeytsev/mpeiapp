package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import androidx.compose.runtime.Immutable
import kekmech.ru.lib_elm.Resource

@Immutable
internal data class BarsAuthMainState(
    val stage: Resource<AuthStage> = Resource.Loading,
)

internal enum class AuthStage {
    LoginPassword,
    TwoFactor,
    AccountList,
    IrrecoverableError,
}

internal sealed interface BarsAuthMainEvent {
    sealed interface Ui : BarsAuthMainEvent {
        data object Init : Ui
    }

    sealed interface Internal : BarsAuthMainEvent
}

internal sealed interface BarsAuthMainCommand

internal sealed interface BarsAuthMainEffect
