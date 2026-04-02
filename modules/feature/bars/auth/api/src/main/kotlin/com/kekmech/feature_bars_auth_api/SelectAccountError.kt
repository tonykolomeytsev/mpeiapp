package com.kekmech.feature_bars_auth_api

public interface SelectAccountError {
    public data object Network : SelectAccountError
    public data object SessionExpired : SelectAccountError
    public data object Fatal : SelectAccountError
}