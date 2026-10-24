package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kotlin.math.roundToInt

/** BARS marks are on a 5-point scale: everything below is a failed control activity */
internal const val PASSING_MARK = 2.5f

/** BARS marks the absence of a mark with -1 */
internal const val NO_MARK = -1f

private val RetakeMarker = Regex("пересд|повтор", RegexOption.IGNORE_CASE)

internal enum class KmStatus {
    /** Passing mark received */
    PASSED,

    /** The mark is marked as a retake, or the received mark is failing and has to be retaken */
    RETAKE,

    /** No passing mark and the control week is over */
    DEBT,

    /** No mark yet, the control week has not passed (or is unknown) */
    UPCOMING,
}

@Immutable
internal data class KmUi(
    val title: String,
    /** Weight in percent of the discipline grade, null if BARS did not report it */
    val weight: String?,
    /** Control week number, null if BARS did not report it */
    val week: Int?,
    /** Mark received by the student, null if there is none */
    val mark: Float?,
    val status: KmStatus,
)

@Immutable
internal data class ScoreUi(
    val label: String,
    val value: Int,
)

@Immutable
internal data class SummaryDisciplineUi(
    val name: String,
    val type: String,
    val person: String,
    val scores: List<ScoreUi>,
    val kms: List<KmUi>,
) {
    val passedCount: Int get() = kms.count { it.status == KmStatus.PASSED }
}

/**
 * Status of a control activity.
 *
 * @param currentWeek number of the current week of the semester, null if it is not known
 */
internal fun ControlActivity.resolveStatus(currentWeek: Int?): KmStatus {
    val hasMark = finalMark != NO_MARK
    val passed = hasMark && finalMark >= PASSING_MARK
    val week = deadline.toIntOrNull()
    return when {
        hasMark && rawMark.contains(RetakeMarker) -> KmStatus.RETAKE
        passed -> KmStatus.PASSED
        hasMark -> KmStatus.RETAKE
        week != null && currentWeek != null && week < currentWeek -> KmStatus.DEBT
        else -> KmStatus.UPCOMING
    }
}

internal fun List<AssessedDiscipline>.toSummary(currentWeek: Int?): List<SummaryDisciplineUi> =
    map { discipline ->
        SummaryDisciplineUi(
            name = discipline.name,
            type = discipline.assessmentType,
            person = discipline.person,
            scores = discipline.finalGrades.mapNotNull { it.toScoreOrNull() },
            kms = discipline.controlActivities.mapIndexed { index, km -> km.toUi(index, currentWeek) },
        )
    }

private fun ControlActivity.toUi(index: Int, currentWeek: Int?) =
    KmUi(
        title = name.ifBlank { "КМ ${index + 1}" },
        weight = weight.takeIf { it.isNotBlank() },
        week = deadline.toIntOrNull(),
        mark = finalMark.takeIf { it != NO_MARK },
        status = resolveStatus(currentWeek),
    )

private fun FinalGrade.toScoreOrNull(): ScoreUi? =
    finalMark.takeIf { it != NO_MARK }?.let { ScoreUi(label = name, value = it.roundToInt()) }
