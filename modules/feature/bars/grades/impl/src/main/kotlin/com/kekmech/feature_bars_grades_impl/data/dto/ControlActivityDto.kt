package com.kekmech.feature_bars_grades_impl.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ControlActivityDto(
    val title: String,
    val weight: Int?,
    val weekNumber: Int?,
    val dateRange: String?,
    val grade: Float?,
    val dateReceived: String?,
)
