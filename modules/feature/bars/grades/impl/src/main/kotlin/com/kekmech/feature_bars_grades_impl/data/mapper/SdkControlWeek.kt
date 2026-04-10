package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.ControlWeek
import com.kekmech.perf_sdk.api.model.grades.ControlWeek as SdkControlWeek

internal fun SdkControlWeek.toDomain(): ControlWeek =
    ControlWeek(
        number = number,
        weekInSemester = weekInSemester,
        grade = grade?.toDomain(),
    )
