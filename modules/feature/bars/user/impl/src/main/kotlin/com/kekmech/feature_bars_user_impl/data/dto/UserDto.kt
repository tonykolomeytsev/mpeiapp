package com.kekmech.feature_bars_user_impl.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("UnsafeOptInUsageError")
@Serializable
internal data class UserDto(
    @SerialName("login")
    val login: String,
    @SerialName("name")
    val name: String,
    @SerialName("group")
    val group: String,
)
