package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.Discipline
import com.kekmech.perf_sdk.api.model.grades.Discipline as SdkDiscipline

internal fun SdkDiscipline.toDomain(): Discipline =
    Discipline(
        title = title,
        teacher = teacher,
        assessmentType = assessmentType,
        totalCredits = totalCredits,
        deadline = deadline,
        activities = activities.map { either -> either.map { it.toDomain() } },
        controlWeeks = controlWeeks.map { either -> either.map { it.toDomain() } },
        intermediateAttestation = intermediateAttestation.map { it?.toDomain() },
        finalGrade = finalGrade?.toDomain(),
    )