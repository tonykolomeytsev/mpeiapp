package com.kekmech.feature_bars_grades_api

public data class ControlActivity(
    val title: String,
    val weight: Int?,
    val weekNumber: Int?,
    val dateRange: String?,
    val grade: Grade?,
    val dateReceived: String?,
)