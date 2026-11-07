package kekmech.ru.feature_bars_impl.data.mapper

import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import io.github.tonykolomeytsev.mpeix.sdk.Grade
import io.github.tonykolomeytsev.mpeix.sdk.Mark
import kekmech.ru.ext_kotlin.capitalizeSafe
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType

private const val NO_MARK = -1f

/**
 * Maps the SDK semester to the old domain models, which the main and details screens still use.
 */
internal object SdkToAssessedDisciplineMapper {

    fun map(disciplines: List<Discipline>): List<AssessedDiscipline> =
        disciplines.map { discipline ->
            AssessedDiscipline(
                name = discipline.name,
                person = discipline.teacher.orEmpty(),
                assessmentType = discipline.assessment.raw.capitalizeSafe(),
                controlActivities = discipline.controlPoints.map { point ->
                    ControlActivity(
                        name = point.name,
                        weight = point.weightPercent?.formatWeight().orEmpty(),
                        deadline = point.week?.toString().orEmpty(),
                        finalMark = point.mark.toFloatMark(),
                    )
                },
                finalGrades = buildList {
                    discipline.currentControlScore?.let {
                        add(FinalGrade("Балл текущего контроля", it.toFloatMark(), FinalGradeType.CURRENT_SCORE))
                    }
                    discipline.controlWeeks.forEach {
                        add(FinalGrade("Контрольная неделя №${it.number}", it.mark.toFloatMark(), FinalGradeType.CONTROL_WEEK))
                    }
                    discipline.attestation?.let {
                        add(FinalGrade("Промежуточная аттестация", it.toFloatMark(), FinalGradeType.INTERMEDIATE_MARK))
                    }
                    discipline.finalGrade?.let {
                        add(FinalGrade("Итоговая оценка", it.toFloatMark(), FinalGradeType.FINAL_MARK))
                    }
                },
            )
        }

    private fun Mark?.toFloatMark(): Float =
        (this?.grade as? Grade.Numeric)?.value?.toFloat() ?: NO_MARK

    private fun Double.formatWeight(): String =
        if (this % 1.0 == 0.0) toInt().toString() else toString()
}
