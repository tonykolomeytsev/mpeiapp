package kekmech.ru.feature_bars_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.Attendance
import kekmech.ru.feature_bars_impl.presentation.compose.StackPart
import kekmech.ru.feature_bars_impl.presentation.compose.StackedBar
import kekmech.ru.feature_bars_impl.presentation.compose.formatPercent
import kekmech.ru.feature_bars_impl.presentation.compose.highlight
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Immutable
internal data class AttendanceBriefUi(
    val total: Int,
    val totalAbsence: Int,
    val excusedAbsence: Int,
) {
    val attended: Int get() = total - totalAbsence
    val unexcusedAbsence: Int get() = totalAbsence - excusedAbsence
}

/** The site's "Пропущено" is `excused + unexcused`. [Attendance.lessons] counts upcoming lessons too. */
internal fun Attendance.toBrief(): AttendanceBriefUi =
    AttendanceBriefUi(
        total = lessons,
        totalAbsence = excused + unexcused,
        excusedAbsence = excused,
    )

/**
 * Brief attendance info: the share of absences without a valid reason and a [StackedBar] with
 * attended classes, excused and unexcused absences. When [AttendanceBriefUi.total] is 0, the bar
 * shows only its empty track.
 */
@Composable
internal fun AttendanceBlock(
    model: AttendanceBriefUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(
                color = MpeixTheme.palette.secondary,
                shape = shape,
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(Strings.bars_attendance_title),
                    style = MpeixTheme.typography.titleSmall,
                    color = MpeixTheme.palette.onSurface,
                )
                if (model.unexcusedAbsence > 0 && model.total > 0) {
                    val locale = LocalConfiguration.current.locales[0]
                    val percent = remember(model, locale) { formatPercent(model.unexcusedAbsence, model.total, locale) }
                    val text = stringResource(Strings.bars_attendance_unexcused, percent)
                    val highlightColor = MpeixTheme.palette.onSurface
                    Text(
                        text = remember(text, percent, highlightColor) {
                            text.highlight(percent, SpanStyle(color = highlightColor))
                        },
                        style = MpeixTheme.typography.bodySmall,
                        color = MpeixTheme.palette.onSurfaceVariant,
                    )
                } else {
                    Text(
                        text = stringResource(Strings.bars_attendance_no_unexcused),
                        style = MpeixTheme.typography.bodySmall,
                        color = MpeixTheme.palette.onSurfaceVariant,
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
        StackedBar(
            parts = listOf(
                StackPart(
                    value = model.attended,
                    color = MpeixTheme.palette.markGreen,
                ),
                StackPart(
                    value = model.excusedAbsence,
                    color = MpeixTheme.palette.outlineVariant,
                ),
                StackPart(
                    value = model.unexcusedAbsence,
                    color = MpeixTheme.palette.markRed,
                )
            ),
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
@PreviewLightDark
private fun AttendanceBlockPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AttendanceBlock(
                model = AttendanceBriefUi(
                    total = 192,
                    totalAbsence = 17,
                    excusedAbsence = 17,
                ),
                onClick = {},
            )
            AttendanceBlock(
                model = AttendanceBriefUi(
                    total = 192,
                    totalAbsence = 17,
                    excusedAbsence = 13,
                ),
                onClick = {},
            )
            AttendanceBlock(
                model = AttendanceBriefUi(
                    total = 192,
                    totalAbsence = 64,
                    excusedAbsence = 20,
                ),
                onClick = {},
            )
        }
    }
}

/** Narrow screen with a large font: the text wraps and the chevron must stay visible. */
@Composable
@Preview(widthDp = 320, fontScale = 1.5f, locale = "ru")
private fun AttendanceBlockLargeFontPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AttendanceBlock(
                model = AttendanceBriefUi(
                    total = 192,
                    totalAbsence = 17,
                    excusedAbsence = 17,
                ),
                onClick = {},
            )
            AttendanceBlock(
                model = AttendanceBriefUi(
                    total = 192,
                    totalAbsence = 64,
                    excusedAbsence = 20,
                ),
                onClick = {},
            )
        }
    }
}
