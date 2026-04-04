package com.kekmech.feature_bars_user_impl.data.mapper

import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.dto.UserDto

internal fun User.toDto(): UserDto =
    UserDto(login = login, name = name, group = group)

internal fun UserDto.toDomain(): User =
    User(login = login, name = name, group = group)
