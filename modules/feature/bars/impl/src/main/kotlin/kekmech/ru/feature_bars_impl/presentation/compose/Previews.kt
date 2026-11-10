package kekmech.ru.feature_bars_impl.presentation.compose

import io.github.tonykolomeytsev.mpeix.sdk.Assessment
import io.github.tonykolomeytsev.mpeix.sdk.ControlPoint
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import io.github.tonykolomeytsev.mpeix.sdk.Grade
import io.github.tonykolomeytsev.mpeix.sdk.Mark

/** [marks] are the control points in order, null is a point without a mark yet. */
internal fun previewDiscipline(
    name: String,
    teacher: String,
    assessment: String,
    finalGrade: Double?,
    vararg marks: Double?,
) = Discipline(
    id = null,
    title = name,
    name = name,
    teacher = teacher,
    supervisor = null,
    assessment = Assessment.Unknown(assessment),
    credits = null,
    deadline = null,
    controlPoints = marks.mapIndexed { i, mark ->
        ControlPoint(
            number = i + 1,
            name = "КМ-${i + 1}. Контрольная работа",
            weightPercent = 9.0,
            week = (i + 1) * 4,
            dueFrom = null,
            dueTo = null,
            mark = mark?.let(::previewMark),
        )
    },
    currentControlScore = null,
    controlWeeks = emptyList(),
    attestation = finalGrade?.let(::previewMark),
    finalGrade = finalGrade?.let(::previewMark),
    isDebt = false,
)

private fun previewMark(value: Double) = Mark(
    raw = value.toString(),
    grade = Grade.Numeric(value),
    date = null,
    isLow = value < 3.0,
    previousAttempts = emptyList(),
)

internal val PreviewDisciplines = listOf(
    previewDiscipline("Иностранный язык", "Чеботарева О.А.", "зачёт с оценкой", finalGrade = 4.0, 5.0, 4.0, 4.0),
    previewDiscipline("Математический анализ", "Петров А.В.", "экзамен", finalGrade = null, 3.0, 4.0),
    previewDiscipline("Физика", "Сидорова Е.Н.", "экзамен", finalGrade = 5.0, 5.0, 5.0, 4.0, 5.0),
    previewDiscipline("Инженерная и компьютерная графика", "Лутошкина Е.Ю.", "зачёт", finalGrade = null),
    previewDiscipline("История России", "Смирнова С.С.", "зачёт", finalGrade = 2.0, 2.0, 3.0),
)
