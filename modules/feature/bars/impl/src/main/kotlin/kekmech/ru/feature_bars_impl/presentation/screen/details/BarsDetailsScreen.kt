package kekmech.ru.feature_bars_impl.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.compose.MarkItem
import kekmech.ru.ui_kit_dialogs.BottomSheet
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private const val NO_MARK = -1f
private val CardShape = RoundedCornerShape(8.dp)

@Composable
internal fun BarsDetailsScreen(discipline: AssessedDiscipline) {
    val finalGrades = remember(discipline) {
        discipline.finalGrades.filter { it.finalMark != NO_MARK }
    }
    BottomSheet {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(rememberNestedScrollInteropConnection()),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item(key = "header") {
                DisciplineHeader(discipline)
            }
            items(discipline.controlActivities) { activity ->
                ControlActivityItem(
                    activity = activity,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
            if (finalGrades.isNotEmpty()) {
                item(key = "final_grades_header") {
                    SectionHeader(
                        title = stringResource(Strings.bars_details_final_grades),
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                    )
                }
                items(finalGrades) { grade ->
                    FinalGradeItem(
                        grade = grade,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            // Intermediate mark is visually separated from the current marks
                            .padding(top = if (grade.type == FinalGradeType.INTERMEDIATE_MARK) 16.dp else 0.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun DisciplineHeader(discipline: AssessedDiscipline) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = discipline.name,
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = MpeixTheme.palette.onSurface)) {
                    append(discipline.assessmentType)
                }
                append(" • ")
                append(discipline.person)
            },
            style = MpeixTheme.typography.titleMedium,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@Composable
private fun ControlActivityItem(
    activity: ControlActivity,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surfaceContainer, CardShape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = activity.name,
            style = MpeixTheme.typography.bodyMedium,
            color = MpeixTheme.palette.onSurface,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            if (activity.weight.isNotEmpty()) {
                ActivityProperty(
                    text = stringResource(Strings.bars_details_weight, activity.weight),
                    value = activity.weight,
                )
            }
            if (activity.deadline.isNotBlank()) {
                ActivityProperty(
                    text = stringResource(Strings.bars_details_deadline, activity.deadline),
                    value = activity.deadline,
                )
            }
            Spacer(Modifier.weight(1f))
            if (activity.finalMark != NO_MARK) {
                MarkItem(
                    mark = activity.finalMark,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

/**
 * Property of the control activity, e.g. "Weight: **9**%", where [value] is highlighted.
 */
@Composable
private fun ActivityProperty(text: String, value: String) {
    val highlightColor = MpeixTheme.palette.onSurface
    val annotatedText = remember(text, value, highlightColor) {
        text.highlight(value, SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold))
    }
    Text(
        text = annotatedText,
        modifier = Modifier.padding(top = 12.dp),
        style = MpeixTheme.typography.bodyMedium,
        color = MpeixTheme.palette.onSurfaceVariant,
    )
}

private fun String.highlight(value: String, style: SpanStyle): AnnotatedString {
    val start = indexOf(value)
    return buildAnnotatedString {
        append(this@highlight)
        if (start >= 0) addStyle(style, start, start + value.length)
    }
}

@Composable
private fun FinalGradeItem(
    grade: FinalGrade,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surfaceContainer, CardShape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = grade.name,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            style = MpeixTheme.typography.bodyMedium,
            fontWeight = if (grade.type == FinalGradeType.FINAL_MARK) FontWeight.Bold else null,
            color = MpeixTheme.palette.onSurface,
        )
        MarkItem(mark = grade.finalMark)
    }
}

@PreviewLightDark
@Composable
private fun BarsDetailsScreenPreview() {
    MpeixTheme {
        BarsDetailsScreen(
            discipline = AssessedDiscipline(
                name = "Вычислительная математика",
                person = "Амосова О.А.",
                assessmentType = "Экзамен",
                controlActivities = listOf(
                    ControlActivity(
                        name = "1. Контрольная работа по теме «Погрешности вычислений»",
                        weight = "9",
                        deadline = "4",
                        finalMark = 5f,
                    ),
                    ControlActivity(
                        name = "2. Контрольная работа по теме «Решение СЛАУ методом Холецкого»",
                        weight = "9",
                        deadline = "8",
                        finalMark = 3f,
                    ),
                    ControlActivity(
                        name = "3. Защита расчетного задания",
                        weight = "12",
                        deadline = "14",
                        finalMark = NO_MARK,
                    ),
                ),
                finalGrades = listOf(
                    FinalGrade(name = "Текущая успеваемость", finalMark = 4.2f, type = FinalGradeType.CURRENT_SCORE),
                    FinalGrade(name = "Контрольная неделя", finalMark = 2f, type = FinalGradeType.CONTROL_WEEK),
                    FinalGrade(name = "Промежуточная аттестация", finalMark = 4f, type = FinalGradeType.INTERMEDIATE_MARK),
                    FinalGrade(name = "Итоговая оценка", finalMark = 4f, type = FinalGradeType.FINAL_MARK),
                ),
            ),
        )
    }
}
