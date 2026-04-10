package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.Grade
import com.kekmech.perf_sdk.api.model.grades.Grade as SdkGrade

internal fun SdkGrade.toDomain(): Grade =
    Grade(float)
