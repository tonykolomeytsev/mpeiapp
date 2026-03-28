package com.kekmech.feature_bars_auth_impl.domain

data class Auth2faProvider(
    val id: String,
    val name: String,
    val default: Boolean,
)
