package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_ui.utils.atStartOfWeek
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate

/**
 * Number of the days in the week calendar: Monday to Saturday
 */
public const val WORKING_DAYS_IN_WEEK: Int = 6

/**
 * # WeekDaysRow
 *
 * Working days (Monday to Saturday) of one week as [DayChip]s,
 * each day takes an equal share of the row width.
 *
 * Replaces the `WeekAdapterItem` grid of the RecyclerView implementation.
 *
 * @param firstDayOfWeek Monday of the week to show, see `atStartOfWeek()`
 * @param selectedDate selected day, it may be out of this week, then no day is selected
 * @param onDayClick called with the date of the clicked day
 * @param modifier [Modifier] to be applied to the row
 * @param today day marked as today
 */
@Composable
public fun WeekDaysRow(
    firstDayOfWeek: LocalDate,
    selectedDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = moscowLocalDate(),
) {
    Row(modifier = modifier.fillMaxWidth()) {
        repeat(WORKING_DAYS_IN_WEEK) { dayIndex ->
            val date = firstDayOfWeek.plusDays(dayIndex.toLong())
            DayChip(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == today,
                onClick = { onDayClick(date) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WeekDaysRowPreview() {
    MpeixTheme {
        val today = LocalDate.of(2026, 11, 13)
        WeekDaysRow(
            firstDayOfWeek = today.atStartOfWeek(),
            selectedDate = today.plusDays(1),
            onDayClick = {},
            today = today,
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(horizontal = 8.dp),
        )
    }
}
