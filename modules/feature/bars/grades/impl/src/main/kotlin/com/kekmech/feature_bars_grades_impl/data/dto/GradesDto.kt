package com.kekmech.feature_bars_grades_impl.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class GradesDto(
    val semesterCode: String?,
    val disciplines: List<DisciplineDto>,
)