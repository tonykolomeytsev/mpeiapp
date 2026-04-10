package com.kekmech.feature_bars_grades_impl.data.mapper

import com.kekmech.feature_bars_grades_api.ControlActivity
import com.kekmech.feature_bars_grades_api.ControlWeek
import com.kekmech.feature_bars_grades_api.Discipline
import com.kekmech.feature_bars_grades_api.Grade
import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_api.Semester
import com.kekmech.feature_bars_grades_impl.data.dto.ControlActivityDto
import com.kekmech.feature_bars_grades_impl.data.dto.ControlWeekDto
import com.kekmech.feature_bars_grades_impl.data.dto.DisciplineDto
import com.kekmech.feature_bars_grades_impl.data.dto.GradesDto

internal fun Grades.toDto(): GradesDto =
    GradesDto(
        semesterCode = semester?.code,
        disciplines = disciplines.map { discipline ->
            DisciplineDto(
                title = discipline.title,
                teacher = discipline.teacher,
                assessmentType = discipline.assessmentType,
                totalCredits = discipline.totalCredits,
                deadline = discipline.deadline,
                activities = discipline.activities.map { either ->
                    either.map { activity ->
                        ControlActivityDto(
                            title = activity.title,
                            weight = activity.weight,
                            weekNumber = activity.weekNumber,
                            dateRange = activity.dateRange,
                            grade = activity.grade?.float,
                            dateReceived = activity.dateReceived,
                        )
                    }.mapLeft { it.message.orEmpty() }
                },
                controlWeeks = discipline.controlWeeks.map { either ->
                    either.map { week ->
                        ControlWeekDto(
                            number = week.number,
                            weekInSemester = week.weekInSemester,
                            grade = week.grade?.float,
                        )
                    }.mapLeft { it.message.orEmpty() }
                },
                intermediateAttestation = discipline.intermediateAttestation
                    .map { it?.float }
                    .mapLeft { it.message.orEmpty() },
                finalGrade = discipline.finalGrade?.float,
            )
        }
    )

internal fun GradesDto.toDomain(): Grades =
    Grades(
        semester = semesterCode?.let(::Semester),
        disciplines = disciplines.map { dto ->
            Discipline(
                title = dto.title,
                teacher = dto.teacher,
                assessmentType = dto.assessmentType,
                totalCredits = dto.totalCredits,
                deadline = dto.deadline,
                activities = dto.activities.map { either ->
                    either.map { activityDto ->
                        ControlActivity(
                            title = activityDto.title,
                            weight = activityDto.weight,
                            weekNumber = activityDto.weekNumber,
                            dateRange = activityDto.dateRange,
                            grade = activityDto.grade?.let(::Grade),
                            dateReceived = activityDto.dateReceived,
                        )
                    }.mapLeft { RuntimeException(it) }
                },
                controlWeeks = dto.controlWeeks.map { either ->
                    either.map { weekDto ->
                        ControlWeek(
                            number = weekDto.number,
                            weekInSemester = weekDto.weekInSemester,
                            grade = weekDto.grade?.let(::Grade),
                        )
                    }.mapLeft { RuntimeException(it) }
                },
                intermediateAttestation = dto.intermediateAttestation
                    .map { it?.let(::Grade) }
                    .mapLeft { RuntimeException(it) },
                finalGrade = dto.finalGrade?.let(::Grade),
            )
        }
    )