package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.asMarkColor
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.io.IOException
import kekmech.ru.res_strings.R.string as Strings

private const val SKELETON_COUNT = 4

/** Content of the "Summary" tab: expandable discipline cards with their control activities (КМ). */
internal fun LazyListScope.summaryItems(
    summary: Resource<List<SummaryDisciplineUi>>,
    expanded: Set<String>,
    onToggle: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (summary) {
        is Resource.Loading -> items(SKELETON_COUNT) { SummaryCardSkeleton(Modifier.cardModifier()) }
        is Resource.Error -> item(key = "summary_error") {
            SummaryError(
                throwable = summary.error,
                onRetry = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }

        is Resource.Data -> {
            if (summary.value.isEmpty()) {
                item(key = "summary_empty") {
                    Text(
                        text = stringResource(Strings.bars_stub_empty_disciplines_description),
                        style = MpeixTheme.typography.paragraphNormal,
                        color = MpeixTheme.palette.contentDisabled,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                    )
                }
            } else {
                items(summary.value, key = { "summary_${it.name}" }) { discipline ->
                    SummaryDisciplineCard(
                        discipline = discipline,
                        isExpanded = discipline.name in expanded,
                        onClick = { onToggle(discipline.name) },
                        modifier = Modifier.cardModifier(),
                    )
                }
            }
        }
    }
}

internal fun Modifier.cardModifier(): Modifier =
    padding(horizontal = 8.dp).padding(bottom = 8.dp)

@Composable
internal fun SummaryDisciplineCard(
    discipline: SummaryDisciplineUi,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "summary_chevron",
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MpeixTheme.palette.surface, shape)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = discipline.name,
                        style = MpeixTheme.typography.paragraphBig,
                        color = MpeixTheme.palette.content,
                    )
                    Text(
                        text = discipline.type + " • " + discipline.person,
                        style = MpeixTheme.typography.paragraphNormal,
                        color = MpeixTheme.palette.contentDisabled,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (isExpanded) Strings.bars_summary_collapse else Strings.bars_summary_expand,
                    ),
                    tint = MpeixTheme.palette.contentVariant,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .rotate(chevronRotation),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                discipline.scores.forEach { score ->
                    Chip(
                        text = "${score.label}: ${score.value}",
                        contentColor = MpeixTheme.palette.content,
                        containerColor = MpeixTheme.palette.surfacePlus3,
                    )
                }
                if (discipline.kms.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            Strings.bars_summary_km_progress,
                            discipline.passedCount,
                            discipline.kms.size,
                        ),
                        style = MpeixTheme.typography.labelNormal,
                        color = MpeixTheme.palette.contentVariant,
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .background(MpeixTheme.palette.surfacePlus1)
                    .padding(vertical = 4.dp),
            ) {
                if (discipline.kms.isEmpty()) {
                    Text(
                        text = stringResource(Strings.bars_summary_no_km),
                        style = MpeixTheme.typography.paragraphNormal,
                        color = MpeixTheme.palette.contentDisabled,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                } else {
                    discipline.kms.forEach { km -> KmRow(km) }
                }
            }
        }
    }
}

@Composable
private fun KmRow(km: KmUi, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = km.title,
                style = MpeixTheme.typography.paragraphNormalAccent,
                color = MpeixTheme.palette.content,
            )
            val meta = buildMeta(km)
            if (meta.isNotEmpty()) {
                Text(
                    text = meta,
                    style = MpeixTheme.typography.labelNormal,
                    color = MpeixTheme.palette.contentDisabled,
                )
            }
            StatusChip(km.status)
        }
        MarkChip(km.mark)
    }
}

@Composable
private fun buildMeta(km: KmUi): String {
    val week = km.week?.let { stringResource(Strings.bars_summary_week, it) }
    val weight = km.weight?.let { stringResource(Strings.bars_summary_weight, it) }
    return listOfNotNull(week, weight).joinToString(" • ")
}

@Composable
private fun MarkChip(mark: Float?) {
    if (mark == null) {
        Chip(
            text = stringResource(Strings.bars_summary_no_mark),
            contentColor = MpeixTheme.palette.contentDisabled,
            containerColor = MpeixTheme.palette.surfacePlus3,
        )
    } else {
        val (content, container) = mark.asMarkColor()
        Chip(
            text = stringResource(Strings.bars_summary_mark, mark.toInt()),
            contentColor = content,
            containerColor = container,
        )
    }
}

/** Status is always shown with an icon and a label, not only with a color */
@Composable
private fun StatusChip(status: KmStatus) {
    val palette = MpeixTheme.palette
    val (icon, label, colors) = when (status) {
        KmStatus.PASSED -> Triple(
            Icons.Default.CheckCircle,
            Strings.bars_km_status_passed,
            palette.greenMarkColor to palette.greenBgMarkColor,
        )

        KmStatus.RETAKE -> Triple(
            Icons.Default.Refresh,
            Strings.bars_km_status_retake,
            palette.yellowMarkColor to palette.yellowBgMarkColor,
        )

        KmStatus.DEBT -> Triple(
            Icons.Default.Warning,
            Strings.bars_km_status_debt,
            palette.redMarkColor to palette.redBgMarkColor,
        )

        KmStatus.UPCOMING -> Triple(
            Icons.Default.DateRange,
            Strings.bars_km_status_upcoming,
            palette.contentVariant to palette.surfacePlus3,
        )
    }
    Chip(
        text = stringResource(label),
        contentColor = colors.first,
        containerColor = colors.second,
        icon = icon,
    )
}

@Composable
private fun Chip(
    text: String,
    contentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = text,
            style = MpeixTheme.typography.paragraphNormalAccent,
            color = contentColor,
        )
    }
}

/** Skeleton of [SummaryDisciplineCard] shown while the marks are loading */
@Composable
internal fun SummaryCardSkeleton(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surface, shape)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SkeletonLine(width = 1f, height = 20.dp)
        SkeletonLine(width = 0.6f, height = 14.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SkeletonLine(width = 0.2f, height = 20.dp)
            SkeletonLine(width = 0.3f, height = 20.dp)
        }
    }
}

@Composable
private fun SkeletonLine(width: Float, height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth(width)
            .height(height)
            .shimmer()
            .background(MpeixTheme.palette.shimmer, RoundedCornerShape(6.dp)),
    )
}

/** Network and parsing failures get different explanations, both can be retried */
@Composable
internal fun SummaryError(
    throwable: Throwable,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isNetwork = remember(throwable) { throwable.isNetworkError() }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MpeixTheme.palette.redMarkColor,
            modifier = Modifier.size(32.dp),
        )
        Text(
            text = stringResource(
                if (isNetwork) Strings.bars_summary_error_network_title else Strings.bars_summary_error_parsing_title,
            ),
            style = MpeixTheme.typography.paragraphBigAccent,
            color = MpeixTheme.palette.content,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(
                if (isNetwork) {
                    Strings.bars_summary_error_network_description
                } else {
                    Strings.bars_summary_error_parsing_description
                },
            ),
            style = MpeixTheme.typography.paragraphNormal,
            color = MpeixTheme.palette.contentVariant,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text(stringResource(Strings.bars_summary_error_retry))
        }
    }
}

/** Timeouts (our own guard included) and I/O problems are connectivity issues, everything else is a data problem */
internal fun Throwable.isNetworkError(): Boolean {
    val source = (this as? ExceptionWithId)?.source ?: this
    return source is IOException || source.message?.startsWith("BARS request timeout") == true
}

@Preview(showBackground = true)
@Composable
private fun SummaryDisciplineCardPreview() {
    val discipline = AssessedDiscipline(
        name = "Теория принятия решений",
        person = "Еремеев А.П.",
        assessmentType = "Экзамен",
        controlActivities = listOf(
            ControlActivity(name = "КМ-1: Контрольная работа", weight = "20", deadline = "4", finalMark = 5f),
            ControlActivity(name = "КМ-2: Лабораторная", weight = "20", deadline = "8", finalMark = 2f),
            ControlActivity(name = "КМ-3: Домашнее задание", weight = "30", deadline = "6", finalMark = -1f),
            ControlActivity(name = "КМ-4: Коллоквиум", weight = "30", deadline = "15", finalMark = -1f),
        ),
        finalGrades = listOf(FinalGrade("Текущий балл", 74f, FinalGradeType.CURRENT_SCORE)),
    )
    MpeixTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val summary = listOf(discipline).toSummary(currentWeek = 10).first()
            SummaryDisciplineCard(summary, isExpanded = true, onClick = {})
            SummaryDisciplineCard(summary, isExpanded = false, onClick = {})
            SummaryCardSkeleton()
        }
    }
}
