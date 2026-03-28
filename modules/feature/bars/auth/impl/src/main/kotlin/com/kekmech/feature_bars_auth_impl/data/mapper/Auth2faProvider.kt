package com.kekmech.feature_bars_auth_impl.data.mapper

import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.perf_sdk.api.model.auth.Auth2faProvider as SdkAuth2faProvider

internal fun SdkAuth2faProvider.toDomain(): Auth2faProvider =
    Auth2faProvider(id = id, name = name, default = default)
