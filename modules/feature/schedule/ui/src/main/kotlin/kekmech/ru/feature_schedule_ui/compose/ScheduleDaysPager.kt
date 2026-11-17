package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.ui_kit_states.ErrorState
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

// Endless in practice, like the weeks of the ScheduleWeeksStrip: about 3 million years each way
private const val DAYS_PAGE_COUNT = Int.MAX_VALUE

// Monday of the current week, in the middle so the days are swiped both ways.
// It's a multiple of the week length, so the day of week of a page is just its remainder
private const val CURRENT_WEEK_MONDAY_PAGE = DAYS_PAGE_COUNT / 2 / WORKING_DAYS_IN_WEEK * WORKING_DAYS_IN_WEEK

private val ListTopPadding = 8.dp

/**
 * Actions of the [ScheduleDaysPager]
 */
public sealed interface ScheduleDaysAction {

    /**
     * The user swiped the pager to the day [dayOfWeek] (1 is Monday) of the week with the [weekOffset].
     * Swiping past Saturday leads to Monday of the next week, and past Monday to Saturday of the previous one.
     * Sent when the finger is released, before the pager finishes settling on the page.
     */
    public data class PageSelected(val weekOffset: Int, val dayOfWeek: Int) : ScheduleDaysAction

    /**
     * The user started dragging the pager
     */
    public data object PagerDragged : ScheduleDaysAction

    /**
     * The day list was scrolled by [dy] pixels, positive [dy] means scrolling to the end of the list
     */
    public data class ClassesScrolled(val dy: Int) : ScheduleDaysAction

    public data class ClassesClick(val classes: Classes) : ScheduleDaysAction
    public data object LunchClick : ScheduleDaysAction
    public data object ReloadClick : ScheduleDaysAction
    public data object SavedScheduleBannerClick : ScheduleDaysAction
}

/**
 * # ScheduleDaysPager
 *
 * Endless horizontal pager of the working days (Monday to Saturday): Saturday is followed
 * by Monday of the next week, so the user swipes from week to week without the week calendar.
 * Each page is the list of the classes of the day.
 *
 * The week offsets are counted from the current week, like in the [ScheduleWeeksStrip].
 * Another week selected outside of the pager (week calendar, FAB) replaces the shown days in place,
 * without scrolling through the days in between.
 *
 * Replaces the `ViewPager2` with the nested `RecyclerView`s of the former `ScheduleFragment`.
 *
 * @param weeks pages of the weeks by their week offsets, see [ScheduleDayPagesConverter].
 * Weeks which are not in the map are shown as loading. The selected week and its neighbours are enough:
 * only the days next to the selected one are visible during a swipe.
 * @param weekOffset offset of the selected week from the current one
 * @param selectedDayOfWeek selected day of the [weekOffset] week, starting from 1 (Monday).
 * The pager scrolls to it when it changes, days out of the range (e.g. Sunday) are ignored.
 * @param onAction called on the user actions
 * @param modifier [Modifier] to be applied to the pager
 * @param savedScheduleBannerText text of the saved schedule banner at the top of each day list
 * of the selected week, or null if the banner is hidden
 * @param isClassesClickable whether the classes cards send [ScheduleDaysAction.ClassesClick]
 * @param bottomPadding space under the end of each day list, e.g. for the FAB, on top of the system bars
 */
@Composable
public fun ScheduleDaysPager(
    weeks: Map<Int, List<ScheduleDayPage>>,
    weekOffset: Int,
    selectedDayOfWeek: Int,
    onAction: (ScheduleDaysAction) -> Unit,
    modifier: Modifier = Modifier,
    savedScheduleBannerText: String? = null,
    isClassesClickable: Boolean = true,
    bottomPadding: Dp = 0.dp,
) {
    val currentOnAction by rememberUpdatedState(onAction)
    val selectedPage = selectedDayOfWeek
        .takeIf { it in 1..WORKING_DAYS_IN_WEEK }
        ?.let { dayPageOf(weekOffset, it) }
    val pagerState = rememberPagerState(initialPage = selectedPage ?: dayPageOf(weekOffset, 1)) {
        DAYS_PAGE_COUNT
    }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    LaunchedEffect(selectedPage) {
        // targetPage is the page the pager is settling to after the swipe, so the selected page
        // reported by the swipe itself doesn't interrupt the fling with another animation.
        // Without a scroll in progress the pager is scrolled even if it's on the selected page already:
        // an interrupted scroll to the previous selected page leaves it between two pages
        if (selectedPage == null) return@LaunchedEffect
        if (pagerState.isScrollInProgress && pagerState.targetPage == selectedPage) return@LaunchedEffect
        isProgrammaticScroll = true
        try {
            if (selectedPage.dayPageWeekOffset() == pagerState.currentPage.dayPageWeekOffset()) {
                pagerState.animateScrollToPage(selectedPage)
            } else {
                // Another week was selected with the week calendar or the FAB: its days take the place
                // of the shown week, as if the pager had only one week
                pagerState.scrollToPage(selectedPage)
            }
        } finally {
            isProgrammaticScroll = false
        }
    }
    LaunchedEffect(pagerState) {
        // Report the page as soon as the finger is released, like ViewPager2.onPageSelected did:
        // during the fling targetPage is already the page the pager settles to.
        // Pages of the programmatic scroll are not reported back to the store.
        snapshotFlow { pagerState.targetPage.takeUnless { isDragged || isProgrammaticScroll } }
            .filterNotNull()
            .distinctUntilChanged()
            .drop(1)
            .collect { page ->
                currentOnAction(ScheduleDaysAction.PageSelected(page.dayPageWeekOffset(), page.dayPageDayOfWeek()))
            }
    }
    LaunchedEffect(pagerState) {
        pagerState.interactionSource.interactions
            .filterIsInstance<DragInteraction.Start>()
            .collect { currentOnAction(ScheduleDaysAction.PagerDragged) }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        key = { it },
        // Neighbour days are composed ahead, so swiping doesn't compose the next page mid-animation.
        // Composing all the days at once made opening the tab ~40% slower (153 vs 44 text measures)
        beyondViewportPageCount = 1,
    ) { page ->
        val pageWeekOffset = page.dayPageWeekOffset()
        val dayIndex = page.dayPageDayOfWeek() - 1
        ScheduleDay(
            page = weeks[pageWeekOffset]?.getOrNull(dayIndex) ?: ScheduleDayPagesConverter.loadingPages[dayIndex],
            savedScheduleBannerText = savedScheduleBannerText.takeIf { pageWeekOffset == weekOffset },
            isClassesClickable = isClassesClickable,
            bottomPadding = bottomPadding,
            onAction = onAction,
        )
    }
}

private fun dayPageOf(weekOffset: Int, dayOfWeek: Int): Int =
    CURRENT_WEEK_MONDAY_PAGE + weekOffset * WORKING_DAYS_IN_WEEK + dayOfWeek - 1

private fun Int.dayPageWeekOffset(): Int =
    Math.floorDiv(this - CURRENT_WEEK_MONDAY_PAGE, WORKING_DAYS_IN_WEEK)

private fun Int.dayPageDayOfWeek(): Int =
    Math.floorMod(this - CURRENT_WEEK_MONDAY_PAGE, WORKING_DAYS_IN_WEEK) + 1

@Composable
private fun ScheduleDay(
    page: ScheduleDayPage,
    savedScheduleBannerText: String?,
    isClassesClickable: Boolean,
    bottomPadding: Dp,
    onAction: (ScheduleDaysAction) -> Unit,
) {
    val currentOnAction by rememberUpdatedState(onAction)
    val listState = rememberLazyListState()
    val hasBanner = savedScheduleBannerText != null
    LaunchedEffect(hasBanner) {
        // LazyColumn keeps the first visible item when the banner is inserted above it,
        // so the banner would stay hidden above the top of the list without this scroll
        if (hasBanner && listState.firstVisibleItemIndex <= 1 && !listState.isScrollInProgress) {
            listState.scrollToItem(0)
        }
    }
    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                // Report only the real scroll of the list, like RecyclerView.OnScrollListener did
                val dy = -consumed.y.roundToInt()
                if (dy != 0) currentOnAction(ScheduleDaysAction.ClassesScrolled(dy))
                return Offset.Zero
            }
        }
    }
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollConnection),
        state = listState,
        contentPadding = PaddingValues(
            top = ListTopPadding,
            bottom = bottomPadding + systemBarsPadding.calculateBottomPadding(),
        ),
    ) {
        if (savedScheduleBannerText != null) {
            item(key = "saved_schedule_banner", contentType = "saved_schedule_banner") {
                SavedScheduleBanner(
                    text = savedScheduleBannerText,
                    onClick = { onAction(ScheduleDaysAction.SavedScheduleBannerClick) },
                    modifier = Modifier
                        .animateItem()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                )
            }
        }
        items(
            items = page.items,
            key = { it.key },
            contentType = { it::class },
        ) { item ->
            ScheduleDayListItem(item, isClassesClickable, onAction)
        }
    }
}

@Composable
private fun LazyItemScope.ScheduleDayListItem(
    item: ScheduleDayItem,
    isClassesClickable: Boolean,
    onAction: (ScheduleDaysAction) -> Unit,
) {
    // Like on the dashboard, the items below slide when a card changes its height,
    // e.g. when a note is attached to the classes
    val itemModifier = Modifier.animateItem()
    val cardModifier = itemModifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    when (item) {
        is ScheduleDayItem.ClassesEntry -> ClassesItem(
            model = item.model,
            onClick = if (isClassesClickable) {
                { onAction(ScheduleDaysAction.ClassesClick(item.classes)) }
            } else {
                null
            },
            modifier = cardModifier,
        )

        is ScheduleDayItem.Window -> WindowItem(itemModifier.padding(horizontal = 8.dp))
        is ScheduleDayItem.Lunch -> LunchItem(
            onClick = { onAction(ScheduleDaysAction.LunchClick) },
            modifier = cardModifier,
        )

        is ScheduleDayItem.SelfStudy -> SelfStudyItem(
            seed = item.date.toEpochDay(),
            modifier = itemModifier.fillParentMaxSize(),
        )
        is ScheduleDayItem.Shimmer -> ScheduleDayShimmer(itemModifier.padding(horizontal = 8.dp))
        is ScheduleDayItem.Error -> ErrorState(
            throwable = item.throwable,
            onReloadClick = { onAction(ScheduleDaysAction.ReloadClick) },
            modifier = itemModifier.padding(top = 24.dp),
        )
    }
}

private val PreviewClasses = Classes(
    name = "Инженерная и компьютерная графика",
    type = ClassesType.LECTURE,
    rawType = null,
    place = "Аудитория Б-407",
    groups = "С-12-16",
    person = "Чахеев Е.Я.",
    time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)),
    number = 1,
)

private val PreviewPages = listOf(
    ScheduleDayPage(
        dayOfWeek = 1,
        items = listOf(
            ScheduleDayItem.ClassesEntry(0, PreviewClasses, PreviewClasses.toUiModel(ScheduleType.GROUP)),
            PreviewClasses.copy(
                name = "Технология металлов",
                type = ClassesType.PRACTICE,
                number = 2,
                time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
                attachedNotePreview = "Принести типовой расчет №1",
            ).let { ScheduleDayItem.ClassesEntry(1, it, it.toUiModel(ScheduleType.GROUP)) },
            ScheduleDayItem.Lunch,
            ScheduleDayItem.Window(3),
            PreviewClasses.copy(
                name = "Физика",
                type = ClassesType.LAB,
                place = "Аудитория Е-306",
                groups = "С-12-16\\1п",
                number = 5,
                time = Time(LocalTime.of(16, 20), LocalTime.of(17, 55)),
            ).let { lab ->
                listOf(lab, lab.copy(place = "Аудитория Е-308", groups = "С-12-16\\2п", person = "Иванов И.И."))
            }.let { ScheduleDayItem.ClassesEntry(4, it.first(), it.toUiModel(ScheduleType.GROUP)) },
        ),
    ),
    ScheduleDayPage(dayOfWeek = 2, items = listOf(ScheduleDayItem.SelfStudy(LocalDate.of(2026, 11, 10)))),
    ScheduleDayPage(dayOfWeek = 3, items = listOf(ScheduleDayItem.Shimmer)),
    ScheduleDayPage(dayOfWeek = 4, items = listOf(ScheduleDayItem.Error(IllegalStateException()))),
)

private val PreviewWeeks = mapOf(0 to PreviewPages)

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            weeks = PreviewWeeks,
            weekOffset = 0,
            selectedDayOfWeek = 1,
            onAction = {},
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerSavedBannerPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            weeks = PreviewWeeks,
            weekOffset = 0,
            selectedDayOfWeek = 1,
            onAction = {},
            savedScheduleBannerText = "Не удалось обновить.\nРасписание от 10 ноября, 14:20",
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerSelfStudyPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            weeks = PreviewWeeks,
            weekOffset = 0,
            selectedDayOfWeek = 2,
            onAction = {},
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerShimmerPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            weeks = PreviewWeeks,
            weekOffset = 0,
            selectedDayOfWeek = 3,
            onAction = {},
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerErrorPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            weeks = PreviewWeeks,
            weekOffset = 0,
            selectedDayOfWeek = 4,
            onAction = {},
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
