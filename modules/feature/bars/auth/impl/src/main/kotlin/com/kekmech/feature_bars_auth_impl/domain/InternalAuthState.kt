package com.kekmech.feature_bars_auth_impl.domain


internal sealed interface InternalAuthState {
    data object Idle : InternalAuthState
    data object LoggedIn : InternalAuthState
    data object LoggedOut : InternalAuthState
    data object SessionExpired : InternalAuthState
    data object VerifyingCredentials : InternalAuthState
    data class Awaiting2faProviderSelection(
        val availableProviders: List<Auth2faProvider>,
    ) : InternalAuthState

    data object RequestingTwoFactorCode : InternalAuthState
    data class Awaiting2faSubmission(
        val availableProviders: List<Auth2faProvider>,
    ) : InternalAuthState

    data object SubmittingTwoFactorCode : InternalAuthState
    data object AwaitingAccountSelection : InternalAuthState
}
