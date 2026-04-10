package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.Semester
import com.kekmech.perf_sdk.api.model.grades.Semester as SdkSemester

internal fun SdkSemester.toDomain(): Semester? =
    Semester(code = parseSemester(text) ?: return null)

private fun parseSemester(input: String): String? {
    val regex = """(\d{4})/(\d{4}).+(Весенний|Осенний)""".toRegex(RegexOption.IGNORE_CASE)
    val matchResult = regex.find(input) ?: return null

    val (startYear, endYear, term) = matchResult.destructured

    return when (term.lowercase()) {
        "весенний" -> "${endYear}h1"
        "осенний" -> "${startYear}h2"
        else -> null
    }
}