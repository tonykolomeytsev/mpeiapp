package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_ui.utils.atStartOfWeek
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalDate
import kekmech.ru.res_strings.R.array as StringArrays

/**
 * Number of the days in the week calendar and the days pager: Monday to Saturday
 */
internal const val WORKING_DAYS_IN_WEEK: Int = 6

// Endless in practice: about 20 million years in each direction from the current week.
// The pager keeps its scroll offset in Long, so the huge page count doesn't overflow
private const val WEEKS_PAGE_COUNT = Int.MAX_VALUE

// Page of the current week, in the middle so the weeks are swiped both ways
private const val CURRENT_WEEK_PAGE = WEEKS_PAGE_COUNT / 2

private val StripHeight = 64.dp

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
 * # ScheduleWeeksStrip
 *
 * Week calendar under the screen header: one week (Monday to Saturday) per page,
 * the weeks are swiped endlessly in both directions.
 *
 * Replaces the endless `RecyclerView` with the `PagerSnapHelper` of the former `ScheduleFragment`.
 *
 * @param weekOffset offset of the selected week from the current one, the strip scrolls to it when it changes
 * @param selectedDate selected day, highlighted in its week
 * @param onWeekSelect called with the week offset when the user swipes to another week
 * @param onDayClick called with the date of the clicked day
 * @param onScroll called when the user starts dragging the strip
 * @param modifier [Modifier] to be applied to the strip
 * @param today current day, the week offsets are counted from its week
 */
@Composable
public fun ScheduleWeeksStrip(
    weekOffset: Int,
    selectedDate: LocalDate,
    onWeekSelect: (Int) -> Unit,
    onDayClick: (LocalDate) -> Unit,
    onScroll: () -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = moscowLocalDate(),
) {
    val currentWeekOffset by rememberUpdatedState(weekOffset)
    val currentOnWeekSelect by rememberUpdatedState(onWeekSelect)
    val currentOnScroll by rememberUpdatedState(onScroll)
    val currentWeekMonday = remember(today) { today.atStartOfWeek() }
    val pagerState = rememberPagerState(initialPage = weekOffset.toWeekPage()) { WEEKS_PAGE_COUNT }
    // Whether the user dragged the strip after the last programmatic scroll. Only the weeks the user
    // swiped to are reported: a programmatic scroll interrupted by the next week selected in the store
    // (fast swipes of the days pager back and forth) stops at a week nobody selected
    var isUserScroll by remember { mutableStateOf(false) }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(weekOffset) {
        // The week changed in the store (FAB, swipe of the days pager), follow it.
        // The fling of the user to this week goes on, otherwise the strip is scrolled even if it's
        // on this page already: the interrupted scroll to the previous week leaves it between two weeks
        val page = weekOffset.toWeekPage()
        if (pagerState.isScrollInProgress && pagerState.targetPage == page) return@LaunchedEffect
        isUserScroll = false
        pagerState.animateScrollToPage(page)
    }
    LaunchedEffect(pagerState) {
        // Report the week the user swiped to once the strip settles, like the RecyclerView did on idle.
        // settledPage alone isn't enough: it takes the page under the finger when the fling starts.
        // isDragged covers the moment between the end of the drag and the start of the fling
        snapshotFlow {
            pagerState.settledPage.takeIf { isUserScroll && !isDragged && !pagerState.isScrollInProgress }
        }
            .filterNotNull()
            .collect { page ->
                isUserScroll = false
                val pageWeekOffset = page - CURRENT_WEEK_PAGE
                if (pageWeekOffset != currentWeekOffset) currentOnWeekSelect(pageWeekOffset)
            }
    }
    LaunchedEffect(pagerState) {
        pagerState.interactionSource.interactions
            .filterIsInstance<DragInteraction.Start>()
            .collect {
                isUserScroll = true
                currentOnScroll()
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(StripHeight),
        key = { it },
    ) { page ->
        // Padding is inside the page, so the neighbour weeks don't peek from the screen edges
        WeekDaysRow(
            firstDayOfWeek = currentWeekMonday.plusWeeks((page - CURRENT_WEEK_PAGE).toLong()),
            selectedDate = selectedDate,
            onDayClick = onDayClick,
            today = today,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}

private fun Int.toWeekPage(): Int =
    (this + CURRENT_WEEK_PAGE).coerceIn(0, WEEKS_PAGE_COUNT - 1)

/**
 * Working days (Monday to Saturday) of one week as [DayChip]s,
 * each day takes an equal share of the row width.
 *
 * Replaces the `WeekAdapterItem` grid of the RecyclerView implementation.
 *
 * @param firstDayOfWeek Monday of the week to show, see `atStartOfWeek()`
 * @param selectedDate selected day, it may be out of this week, then no day is selected
 * @param onDayClick called with the date of the clicked day
 * @param today day marked as today
 */
@Composable
private fun WeekDaysRow(
    firstDayOfWeek: LocalDate,
    selectedDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit,
    today: LocalDate,
    modifier: Modifier = Modifier,
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

/**
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
private fun DayChip(
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
private fun ScheduleWeeksStripPreview() {
    MpeixTheme {
        val today = LocalDate.of(2026, 11, 13)
        ScheduleWeeksStrip(
            weekOffset = 0,
            selectedDate = today,
            onWeekSelect = {},
            onDayClick = {},
            onScroll = {},
            today = today,
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(vertical = 4.dp),
        )
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
