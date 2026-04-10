package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.ControlActivity
import com.kekmech.perf_sdk.api.model.grades.ControlActivity as SdkControlActivity

internal fun SdkControlActivity.toDomain(): ControlActivity =
    ControlActivity(
        title = title,
        weight = weight,
        weekNumber = weekNumber,
        dateRange = dateRange,
        grade = grade?.toDomain(),
        dateReceived = dateReceived,
    )
