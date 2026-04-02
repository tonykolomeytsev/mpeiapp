package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import androidx.compose.runtime.Immutable
import com.kekmech.feature_bars_auth_api.SelectAccountError
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState

@Immutable
internal data class BarsAuthMainState(
    val stage: AuthStage = AuthStage.Loading,
)

internal enum class AuthStage {
    LoginPassword,
    TwoFactor,
    IrrecoverableError,
    Loading,
}

internal sealed interface BarsAuthMainEvent {
    sealed interface Ui : BarsAuthMainEvent {
        data object Init : Ui
    }

    sealed interface Internal : BarsAuthMainEvent {
        data class SubscribeInternalAuthStateSuccess(
            val state: InternalAuthState,
            val hasFatalError: Boolean,
        ) : Internal

        data object SelectAccountAutomaticallySuccess : Internal
        data class SelectAccountAutomaticallyFailure(
            val selectAccountError: SelectAccountError,
        ) : Internal
    }
}

internal sealed interface BarsAuthMainCommand {
    data object SubscribeInternalAuthState : BarsAuthMainCommand
    data object CloseLoginFlow : BarsAuthMainCommand
    data class SelectAccountAutomatically(val afterRetry: Boolean) : BarsAuthMainCommand
}

internal sealed interface BarsAuthMainEffect
