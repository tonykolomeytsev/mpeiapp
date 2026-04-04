package com.kekmech.feature_bars_user_impl.data.mapper

import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.dto.UserDto
import kotlin.test.Test
import kotlin.test.assertEquals

class UserMapperTest {

    @Test
    fun `test toDomain mapping`() {
        // Given
        val dto = UserDto(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val expected = User(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )

        // When
        val actual = dto.toDomain()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `test toDto mapping`() {
        // Given
        val domain = User(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )
        val expected = UserDto(
            login = "PupkinVV",
            name = "Пупкин Василий Васильевич",
            group = "Х-12-34",
        )

        // When
        val actual = domain.toDto()

        // Then
        assertEquals(expected, actual)
    }
}