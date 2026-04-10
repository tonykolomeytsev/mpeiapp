@file:UseSerializers(EitherSerializer::class)

package com.kekmech.feature_bars_grades_impl.data.dto

import arrow.core.Either
import arrow.core.serialization.EitherSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
internal data class DisciplineDto(
    val title: String,
    val teacher: String?,
    val assessmentType: String?,
    val totalCredits: Float?,
    val deadline: String?,
    val activities: List<Either<String, ControlActivityDto>>,
    val controlWeeks: List<Either<String, ControlWeekDto>>,
    val intermediateAttestation: Either<String, Float?>,
    val finalGrade: Float?,
)
