package com.kekmech.feature_bars_auth_impl.domain

internal sealed interface GetAccountsError {
    data object Network : GetAccountsError
    data object SessionExpired : GetAccountsError
    data object Fatal : GetAccountsError
}