package com.kekmech.feature_bars_grades_impl.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ControlWeekDto(
    val number: Int,
    val weekInSemester: Int,
    val grade: Float?,
)
