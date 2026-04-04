package com.kekmech.feature_bars_user_impl.data.datasource

import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.dto.UserDto
import com.tencent.mmkv.MMKV
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class CachedUserDataSourceTest {

    @Test
    fun `test writing to cached data source`() {
        // Given
        val mmkv = mockk<MMKV>(relaxed = true)
        val dataSource = CachedUserDataSource(cache = mmkv)
        val domain = User(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val dto = UserDto(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val expectedBytes = Cbor.encodeToByteArray(dto)

        // When
        dataSource.put(domain)

        // Then
        verify(exactly = 1) { mmkv.encode("current_user", expectedBytes) }
    }

    @Test
    fun `test reading from cached data source`() {
        // Given
        val mmkv = mockk<MMKV>(relaxed = true)
        val dataSource = CachedUserDataSource(cache = mmkv)
        val expected = User(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val dto = UserDto(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val bytes = Cbor.encodeToByteArray(dto)
        every { mmkv.decodeBytes("current_user") } returns bytes


        // When
        val actual = dataSource.get()

        // Then
        assertEquals(expected, actual)
    }
}