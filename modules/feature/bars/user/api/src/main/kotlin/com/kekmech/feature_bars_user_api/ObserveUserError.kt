package com.kekmech.feature_bars_user_api

public sealed interface ObserveUserError {
    public data object Network : ObserveUserError
    public data object SessionExpired : ObserveUserError
    public data object Internal : ObserveUserError
}
