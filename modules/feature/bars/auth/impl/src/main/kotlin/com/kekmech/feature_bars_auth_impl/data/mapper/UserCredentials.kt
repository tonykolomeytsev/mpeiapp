package com.kekmech.feature_bars_auth_impl.data.mapper

import com.kekmech.feature_bars_auth_impl.domain.UserCredentials
import com.kekmech.perf_sdk.api.model.auth.AuthCredentialsData

internal fun UserCredentials.toDto(): AuthCredentialsData =
    AuthCredentialsData(
        username = login,
        password = password,
        remember = false,
    )
