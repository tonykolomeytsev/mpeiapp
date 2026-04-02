package com.kekmech.feature_bars_auth_impl.data.mapper

import com.kekmech.feature_bars_auth_impl.domain.InternalAuthAccount
import com.kekmech.perf_sdk.api.model.auth.AuthAccount

internal fun AuthAccount.toDomain(): InternalAuthAccount =
    InternalAuthAccount(id = id, name = name, group = group)