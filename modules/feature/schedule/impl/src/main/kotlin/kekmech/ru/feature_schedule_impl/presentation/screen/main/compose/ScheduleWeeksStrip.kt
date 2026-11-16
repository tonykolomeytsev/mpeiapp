package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_ui.compose.WeekDaysRow
import kekmech.ru.feature_schedule_ui.utils.atStartOfWeek
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import java.time.LocalDate

// Weeks reachable in each direction from the current one, about 20 years in total.
// The page count is limited because the pager measures the scroll offset of all the pages before
// the current one, which overflows with Int.MAX_VALUE pages of the RecyclerView implementation
private const val WEEKS_IN_EACH_DIRECTION = 520
private const val WEEKS_PAGE_COUNT = WEEKS_IN_EACH_DIRECTION * 2 + 1

// Page of the current week
private const val CURRENT_WEEK_PAGE = WEEKS_IN_EACH_DIRECTION

private val StripHeight = 64.dp

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
internal fun ScheduleWeeksStrip(
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
    val pagerState = rememberPagerState(initialPage = weekOffset.toPage()) { WEEKS_PAGE_COUNT }

    LaunchedEffect(weekOffset) {
        // The week changed in the store (FAB, swipe of the days pager), follow it
        val page = weekOffset.toPage()
        if (pagerState.targetPage != page) pagerState.animateScrollToPage(page)
    }
    LaunchedEffect(pagerState) {
        // Report the week the user swiped to once the strip settles, like the RecyclerView did on idle.
        // The week of a programmatic scroll is the selected one already, so it's not reported back
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .drop(1)
            .collect { page ->
                val pageWeekOffset = page - CURRENT_WEEK_PAGE
                if (pageWeekOffset != currentWeekOffset) currentOnWeekSelect(pageWeekOffset)
            }
    }
    LaunchedEffect(pagerState) {
        pagerState.interactionSource.interactions
            .filterIsInstance<DragInteraction.Start>()
            .collect { currentOnScroll() }
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

private fun Int.toPage(): Int =
    (this + CURRENT_WEEK_PAGE).coerceIn(0, WEEKS_PAGE_COUNT - 1)

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
