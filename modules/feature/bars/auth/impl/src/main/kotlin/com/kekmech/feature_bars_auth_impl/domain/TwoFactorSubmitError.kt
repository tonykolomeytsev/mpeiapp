package com.kekmech.feature_bars_auth_impl.domain

internal sealed interface TwoFactorSubmitError {
    data object Network : TwoFactorSubmitError
    data object WrongCode : TwoFactorSubmitError
    data object Fatal : TwoFactorSubmitError
}
