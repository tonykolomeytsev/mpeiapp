package kekmech.ru.feature_bars_impl.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.ControlPoint
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import kekmech.ru.ext_kotlin.capitalizeSafe
import kekmech.ru.feature_bars_impl.navigation.BarsDetailsScreenKey
import kekmech.ru.feature_bars_impl.presentation.compose.MarkItem
import kekmech.ru.feature_bars_impl.presentation.compose.numeric
import kekmech.ru.feature_bars_impl.presentation.compose.previewDiscipline
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kekmech.ru.res_strings.R.string as Strings

private val CardShape = RoundedCornerShape(8.dp)

/** Row of the final grades section. Only numeric marks get a row, as there is no chip for "зачтено". */
private class FinalGrade(
    val name: String,
    val mark: Float,
    val isAttestation: Boolean = false,
    val isFinal: Boolean = false,
)

@Composable
internal fun BarsDetailsScreen(key: BarsDetailsScreenKey) {
    val viewModel = koinViewModel<BarsDetailsViewModel> { parametersOf(key) }
    val discipline by viewModel.discipline.collectAsState()
    discipline?.let { BarsDetailsScreen(discipline = it) }
}

/** Content of the details bottom sheet, the sheet itself is shown by the navigation. */
@Composable
internal fun BarsDetailsScreen(discipline: Discipline) {
    val finalGrades = remember(discipline) { discipline.finalGrades() }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item(key = "header") {
            DisciplineHeader(discipline)
        }
        items(discipline.controlPoints) { point ->
            ControlPointItem(
                point = point,
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
                        .padding(top = if (grade.isAttestation) 16.dp else 0.dp),
                )
            }
        }
    }
}

private fun Discipline.finalGrades(): List<FinalGrade> = buildList {
    currentControlScore?.numeric?.let { add(FinalGrade("Балл текущего контроля", it)) }
    controlWeeks.forEach { week ->
        week.mark?.numeric?.let { add(FinalGrade("Контрольная неделя №${week.number}", it)) }
    }
    attestation?.numeric?.let {
        add(
            FinalGrade(
                "Промежуточная аттестация",
                it,
                isAttestation = true
            )
        )
    }
    finalGrade?.numeric?.let { add(FinalGrade("Итоговая оценка", it, isFinal = true)) }
}

@Composable
private fun DisciplineHeader(discipline: Discipline) {
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
                    append(discipline.assessment.raw.capitalizeSafe())
                }
                discipline.teacher?.let {
                    append(" • ")
                    append(it)
                }
            },
            style = MpeixTheme.typography.titleMedium,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@Composable
private fun ControlPointItem(
    point: ControlPoint,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surfaceContainer, CardShape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = point.name,
            style = MpeixTheme.typography.bodyMedium,
            color = MpeixTheme.palette.onSurface,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            point.weightPercent?.formatWeight()?.let { weight ->
                ActivityProperty(
                    text = stringResource(Strings.bars_details_weight, weight),
                    value = weight,
                )
            }
            point.week?.toString()?.let { week ->
                ActivityProperty(
                    text = stringResource(Strings.bars_details_deadline, week),
                    value = week,
                )
            }
            Spacer(Modifier.weight(1f))
            point.mark?.numeric?.let { mark ->
                MarkItem(
                    mark = mark,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

private fun Double.formatWeight(): String =
    if (this % 1.0 == 0.0) toInt().toString() else toString()

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
            fontWeight = if (grade.isFinal) FontWeight.Bold else null,
            color = MpeixTheme.palette.onSurface,
        )
        MarkItem(mark = grade.mark)
    }
}

@PreviewLightDark
@Composable
private fun BarsDetailsScreenPreview() {
    MpeixTheme {
        BarsDetailsScreen(
            discipline = previewDiscipline(
                name = "Вычислительная математика",
                teacher = "Амосова О.А.",
                assessment = "экзамен",
                finalGrade = 4.0,
                5.0, 3.0, null,
            ),
        )
    }
}
