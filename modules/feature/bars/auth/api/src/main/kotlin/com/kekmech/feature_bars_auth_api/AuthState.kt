package com.kekmech.feature_bars_auth_api

/** Simplified representation of the auth state for external modules */
public sealed interface AuthState {
    public data object Idle : AuthState
    public data object LoggedIn : AuthState
    public data object LoggedOut : AuthState
    public data object SessionExpired : AuthState
}
