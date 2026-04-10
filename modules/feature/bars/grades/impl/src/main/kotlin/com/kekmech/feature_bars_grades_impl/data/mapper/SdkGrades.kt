package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.perf_sdk.api.model.grades.GradesInfo

internal fun GradesInfo.toDomain(): Grades =
    Grades(
        semester = semester.map { it.toDomain() }.getOrNull(),
        disciplines = disciplines.map { it.toDomain() },
    )