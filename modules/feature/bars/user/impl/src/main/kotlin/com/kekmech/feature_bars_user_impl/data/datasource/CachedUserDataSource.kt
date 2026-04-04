package com.kekmech.feature_bars_user_impl.data.datasource

import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.dto.UserDto
import com.kekmech.feature_bars_user_impl.data.mapper.toDomain
import com.kekmech.feature_bars_user_impl.data.mapper.toDto
import com.tencent.mmkv.MMKV
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

@OptIn(ExperimentalSerializationApi::class)
internal class CachedUserDataSource(
    private val cache: MMKV = MMKV.mmkvWithID("user_cache")
) {

    fun put(user: User) {
        val dto = user.toDto()
        val bytes = Cbor.encodeToByteArray(dto)
        cache.encode(CURRENT_USER_KEY, bytes)
    }

    fun get(): User? {
        val bytes = cache.decodeBytes(CURRENT_USER_KEY) ?: return null
        val dto = Cbor.decodeFromByteArray<UserDto>(bytes)
        return dto.toDomain()
    }

    private companion object {
        private const val CURRENT_USER_KEY = "current_user"
    }
}