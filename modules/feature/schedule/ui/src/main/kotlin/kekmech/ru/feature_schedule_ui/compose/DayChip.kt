package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import kekmech.ru.res_strings.R.array as StringArrays

private val ChipHeight = 72.dp
private val ChipContentWidth = 44.dp
private val ChipContentHeight = 64.dp
private val ChipShape = RoundedCornerShape(8.dp)

// Labels are stacked tighter than the theme line heights allow, as in the RecyclerView implementation:
// the month name even sits a bit closer to the day number
private val DayTextLineHeight = 19.sp
private val MonthTextLineHeight = 12.sp
private val MonthNameOffset = (-2).dp

/**
 * # DayChip
 *
 * One day of the week calendar: short day of week name, day of month and short month name.
 * The selected day is filled with the primary color, today is marked with a dot under the chip.
 *
 * The chip is 72dp high and takes the width given by the [modifier],
 * the 44dp wide content is centered horizontally.
 *
 * Replaces the `DayAdapterItem` of the RecyclerView implementation.
 *
 * @param date day to show
 * @param isSelected whether the day is selected
 * @param isToday whether the day is today, see `moscowLocalDate()`
 * @param onClick called when the chip is clicked
 * @param modifier [Modifier] to be applied to the chip
 */
@Composable
public fun DayChip(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayOfWeekName = stringArrayResource(StringArrays.days_of_week_short)
        .getOrNull(date.dayOfWeek.value)
        .orEmpty()
    val monthName = stringArrayResource(StringArrays.months_short)
        .getOrNull(date.monthValue - 1)
        .orEmpty()
    val palette = MpeixTheme.palette
    val primaryTextColor = if (isSelected) palette.onPrimary else palette.onSurface
    val secondaryTextColor = if (isSelected) palette.onPrimary else palette.onSurfaceVariant

    Box(
        modifier = modifier.height(ChipHeight),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .size(ChipContentWidth, ChipContentHeight)
                .clip(ChipShape)
                .background(if (isSelected) palette.primary else Color.Transparent)
                .selectable(selected = isSelected, onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = dayOfWeekName,
                style = MpeixTheme.typography.titleMedium,
                color = primaryTextColor,
                lineHeight = DayTextLineHeight,
                maxLines = 1,
            )
            Text(
                text = date.dayOfMonth.toString(),
                style = MpeixTheme.typography.titleMedium,
                color = primaryTextColor,
                lineHeight = DayTextLineHeight,
                maxLines = 1,
            )
            Text(
                text = monthName,
                style = MpeixTheme.typography.labelSmall,
                color = secondaryTextColor,
                lineHeight = MonthTextLineHeight,
                maxLines = 1,
                modifier = Modifier.offset(y = MonthNameOffset),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DayChipPreview() {
    MpeixTheme {
        val date = LocalDate.of(2026, 11, 16)
        Row(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
        ) {
            DayChip(date, isSelected = false, isToday = false, onClick = {}, Modifier.width(64.dp))
            DayChip(date.plusDays(1), isSelected = true, isToday = false, onClick = {}, Modifier.width(64.dp))
            DayChip(date.plusDays(2), isSelected = false, isToday = true, onClick = {}, Modifier.width(64.dp))
            DayChip(date.plusDays(3), isSelected = true, isToday = true, onClick = {}, Modifier.width(64.dp))
        }
    }
}
