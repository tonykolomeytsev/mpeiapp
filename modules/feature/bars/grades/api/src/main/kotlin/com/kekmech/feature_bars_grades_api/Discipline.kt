package com.kekmech.feature_bars_grades_api

import arrow.core.Either

public data class Discipline(
    val title: String,
    val teacher: String?,
    val assessmentType: String?,
    val totalCredits: Float?,
    val deadline: String?,
    val activities: List<Either<Throwable, ControlActivity>>,
    val controlWeeks: List<Either<Throwable, ControlWeek>>,
    val intermediateAttestation: Either<Throwable, Grade?>,
    val finalGrade: Grade?,
)