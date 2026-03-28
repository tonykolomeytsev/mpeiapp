package com.kekmech.feature_bars_auth_impl.domain

internal sealed interface TwoFactorRequestError {
    data object Network : TwoFactorRequestError
    data class Message(val message: String) : TwoFactorRequestError
    data object Fatal : TwoFactorRequestError
}