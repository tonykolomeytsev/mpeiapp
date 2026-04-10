package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.Semester
import io.mockk.every
import io.mockk.mockk
import com.kekmech.perf_sdk.api.model.grades.Semester as SdkSemester
import kotlin.test.Test
import kotlin.test.assertEquals

class SdkSemesterTest {

    @Test
    fun `from sdk to domain 2026h1`() {
        // Given
        val sdkModel = mockk<SdkSemester>(relaxed = true) {
            every { text } returns "2025/2026, Весенний семестр"
        }
        val expected = Semester("2026h1")

        // When
        val actual = sdkModel.toDomain()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `from sdk to domain 2025h2`() {
        // Given
        val sdkModel = mockk<SdkSemester>(relaxed = true) {
            every { text } returns "2025/2026, Осенний семестр"
        }
        val expected = Semester("2025h2")

        // When
        val actual = sdkModel.toDomain()

        // Then
        assertEquals(expected, actual)
    }


    @Test
    fun `from sdk to domain 2025h1`() {
        // Given
        val sdkModel = mockk<SdkSemester>(relaxed = true) {
            every { text } returns "2024/2025, Весенний семестр"
        }
        val expected = Semester("2025h1")

        // When
        val actual = sdkModel.toDomain()

        // Then
        assertEquals(expected, actual)
    }


    @Test
    fun `from sdk to domain 2024h2`() {
        // Given
        val sdkModel = mockk<SdkSemester>(relaxed = true) {
            every { text } returns "2024/2025, Осенний семестр"
        }
        val expected = Semester("2024h2")

        // When
        val actual = sdkModel.toDomain()

        // Then
        assertEquals(expected, actual)
    }
}