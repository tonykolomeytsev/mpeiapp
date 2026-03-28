package com.kekmech.feature_bars_auth_impl.domain

sealed interface LoginPasswordError {
    data object Network : LoginPasswordError
    data object InvalidCredentials : LoginPasswordError
    data object Fatal : LoginPasswordError
}