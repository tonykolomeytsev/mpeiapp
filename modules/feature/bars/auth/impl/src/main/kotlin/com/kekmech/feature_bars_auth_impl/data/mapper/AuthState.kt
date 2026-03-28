package com.kekmech.feature_bars_auth_impl.data.mapper

import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.perf_sdk.api.model.auth.AuthState as SdkAuthState

internal fun SdkAuthState.toDomain(): InternalAuthState =
    when (this) {
        is SdkAuthState.Idle -> InternalAuthState.Idle
        is SdkAuthState.LoggedIn -> InternalAuthState.LoggedIn
        is SdkAuthState.LoggedOut -> InternalAuthState.LoggedOut
        is SdkAuthState.SessionExpired -> InternalAuthState.SessionExpired
        is SdkAuthState.VerifyingCredentials -> InternalAuthState.VerifyingCredentials
        is SdkAuthState.Awaiting2faProviderSelection -> InternalAuthState.Awaiting2faProviderSelection(
            availableProviders = availableProviders.map { it.toDomain() },
        )

        is SdkAuthState.Awaiting2faSubmission -> InternalAuthState.Awaiting2faSubmission(
            availableProviders = availableProviders.map { it.toDomain() },
        )

        is SdkAuthState.RequestingTwoFactorCode -> InternalAuthState.RequestingTwoFactorCode
        is SdkAuthState.SubmittingTwoFactorCode -> InternalAuthState.SubmittingTwoFactorCode
        is SdkAuthState.AwaitingAccountSelection -> InternalAuthState.AwaitingAccountSelection
    }