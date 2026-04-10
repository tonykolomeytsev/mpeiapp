package com.kekmech.feature_bars_grades_impl.data.datastore

import com.kekmech.feature_bars_grades_impl.data.Mocks
import com.kekmech.feature_bars_grades_impl.data.datasource.CachedGradesDataSource
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
class CachedGradesDataSourceTest {

    @Test
    fun `test writing to cached data source`() {
        // Given
        val mmkv = mockk<MMKV>(relaxed = true)
        val dataSource = CachedGradesDataSource(cache = mmkv)
        val domain = Mocks.GRADES
        val dto = Mocks.GRADES_DTO
        val expectedBytes = Cbor.encodeToByteArray(dto)

        // When
        dataSource.put(domain)

        // Then
        verify(exactly = 1) { mmkv.encode("semester_2026h1", expectedBytes) }
    }

    @Test
    fun `test reading from cached data source`() {
        // Given
        val mmkv = mockk<MMKV>(relaxed = true)
        val dataSource = CachedGradesDataSource(cache = mmkv)
        val expected = Mocks.GRADES
        val dto = Mocks.GRADES_DTO
        val bytes = Cbor.encodeToByteArray(dto)
        every { mmkv.decodeBytes("semester_2026h1") } returns bytes

        // When
        val actual = dataSource.get("2026h1")

        // Then
        assertEquals(expected, actual)
    }
}