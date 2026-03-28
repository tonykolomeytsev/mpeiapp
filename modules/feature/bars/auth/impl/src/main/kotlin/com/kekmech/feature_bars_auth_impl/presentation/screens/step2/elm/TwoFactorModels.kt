package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorRequestError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorSubmitError

internal data class TwoFactorState(
    val providers: List<Auth2faProvider> = emptyList(),
    val codeState: CodeState = CodeState.Initial,
    val debounceSec: Int = -1,
    val isLoading: Boolean = false,
)

internal sealed interface CodeState {
    data object Initial : CodeState
    data class SendingCode(val provider: Auth2faProvider) : CodeState
    data class CodeSent(val provider: Auth2faProvider) : CodeState
}

internal sealed interface TwoFactorEvent {
    sealed interface Ui : TwoFactorEvent {
        data object Init : Ui

        sealed interface Action : Ui {
            data class SubmitCode(val code: String) : Action
            data class ResendCode(val provider: Auth2faProvider) : Action
        }
    }

    sealed interface Internal : TwoFactorEvent {
        data class RequestTwoFactorCodeSuccess(
            val provider: Auth2faProvider,
            val debounceSec: Int,
        ) : Internal

        data class RequestTwoFactorCodeFailure(val error: TwoFactorRequestError) : Internal

        data class GetAuth2faProvidersSuccess(val providers: List<Auth2faProvider>) : Internal
        data class SubscribeTwoFactorCodeTimerSuccess(val secRemains: Int) : Internal

        data object Submit2faCodeSuccess : Internal
        data class Submit2faCodeFailure(val error: TwoFactorSubmitError) : Internal
    }
}

internal sealed interface TwoFactorCommand {
    data object GetAuth2faProviders : TwoFactorCommand
    data class RequestTwoFactorCode(val provider: Auth2faProvider) : TwoFactorCommand
    data object SubscribeTwoFactorCodeTimer : TwoFactorCommand
    data class Submit2faCode(val code: String) : TwoFactorCommand
}

internal sealed interface TwoFactorEffect {
    data object ShowInvalidCodeAlert : TwoFactorEffect
    data object ShowNetworkErrorBanner : TwoFactorEffect
    data object ShowFatalErrorBanner : TwoFactorEffect
    data class ShowServerErrorBanner(val message: String) : TwoFactorEffect
}
