package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

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
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.compose.ClassesItem
import kekmech.ru.feature_schedule_ui.compose.LunchItem
import kekmech.ru.feature_schedule_ui.compose.SelfStudyItem
import kekmech.ru.feature_schedule_ui.compose.WindowItem
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.ui_kit_states.ErrorState
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalTime
import kotlin.math.roundToInt

private val ListTopPadding = 8.dp

// Space for the bottom navigation bar and the FAB
private val ListBottomPadding = 144.dp

/**
 * Actions of the [ScheduleDaysPager], handled by the `ScheduleFragment`
 */
internal sealed interface ScheduleDaysAction {

    /**
     * The user swiped the pager to the [page]. Sent when the finger is released,
     * before the pager finishes settling on the page.
     */
    data class PageSelected(val page: Int) : ScheduleDaysAction

    /**
     * The user started dragging the pager
     */
    data object PagerDragged : ScheduleDaysAction

    /**
     * The day list was scrolled by [dy] pixels, positive [dy] means scrolling to the end of the list
     */
    data class ClassesScrolled(val dy: Int) : ScheduleDaysAction

    data class ClassesClick(val classes: Classes) : ScheduleDaysAction
    data object LunchClick : ScheduleDaysAction
    data object ReloadClick : ScheduleDaysAction
    data object SavedScheduleBannerClick : ScheduleDaysAction
}

/**
 * # ScheduleDaysPager
 *
 * Horizontal pager of the working days (Monday to Saturday) of the selected week.
 * Each page is the list of the classes of the day.
 *
 * Replaces the `ViewPager2` with the nested `RecyclerView`s of the `ScheduleFragment`.
 *
 * @param pages pages to show, see `ScheduleClassesListConverter`
 * @param selectedPage page of the selected day, the pager scrolls to it when it changes.
 * Pages out of the range (e.g. Sunday) are ignored.
 * @param onAction called on the user actions
 * @param savedScheduleBannerText text of the saved schedule banner at the top of each day list,
 * or null if the banner is hidden
 * @param modifier [Modifier] to be applied to the pager
 * @param initialPage page shown on the first composition, pass the [selectedPage] if it's in range,
 * so the pager doesn't scroll to it right after opening
 */
@Composable
internal fun ScheduleDaysPager(
    pages: List<ScheduleDayPage>,
    selectedPage: Int,
    onAction: (ScheduleDaysAction) -> Unit,
    savedScheduleBannerText: String? = null,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
) {
    val currentOnAction by rememberUpdatedState(onAction)
    val pagerState = rememberPagerState(initialPage = initialPage) { pages.size }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    LaunchedEffect(selectedPage) {
        // targetPage is the page the pager is settling to after the swipe, so the selected page
        // reported by the swipe itself doesn't interrupt the fling with another animation
        if (selectedPage !in 0 until pagerState.pageCount || pagerState.targetPage == selectedPage) {
            return@LaunchedEffect
        }
        isProgrammaticScroll = true
        try {
            pagerState.animateScrollToPage(selectedPage)
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
            .collect { currentOnAction(ScheduleDaysAction.PageSelected(it)) }
    }
    LaunchedEffect(pagerState) {
        pagerState.interactionSource.interactions
            .filterIsInstance<DragInteraction.Start>()
            .collect { currentOnAction(ScheduleDaysAction.PagerDragged) }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        key = { pages[it].dayOfWeek },
        // All days are composed at once, so swiping doesn't compose the next page mid-animation
        beyondViewportPageCount = (pages.size - 1).coerceAtLeast(0),
    ) { page ->
        ScheduleDay(
            page = pages[page],
            savedScheduleBannerText = savedScheduleBannerText,
            onAction = onAction,
        )
    }
}

@Composable
private fun ScheduleDay(
    page: ScheduleDayPage,
    savedScheduleBannerText: String?,
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
            bottom = ListBottomPadding + systemBarsPadding.calculateBottomPadding(),
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
            ScheduleDayListItem(item, onAction)
        }
    }
}

@Composable
private fun LazyItemScope.ScheduleDayListItem(
    item: ScheduleDayItem,
    onAction: (ScheduleDaysAction) -> Unit,
) {
    val itemModifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    when (item) {
        is ScheduleDayItem.ClassesEntry -> ClassesItem(
            model = item.model,
            onClick = { onAction(ScheduleDaysAction.ClassesClick(item.classes)) },
            modifier = itemModifier,
        )

        is ScheduleDayItem.Window -> WindowItem(Modifier.padding(horizontal = 8.dp))
        is ScheduleDayItem.Lunch -> LunchItem(
            onClick = { onAction(ScheduleDaysAction.LunchClick) },
            modifier = itemModifier,
        )

        is ScheduleDayItem.SelfStudy -> SelfStudyItem(Modifier.fillParentMaxSize())
        is ScheduleDayItem.Shimmer -> ScheduleDayShimmer(Modifier.padding(horizontal = 8.dp))
        is ScheduleDayItem.Error -> ErrorState(
            throwable = item.throwable,
            onReloadClick = { onAction(ScheduleDaysAction.ReloadClick) },
            modifier = Modifier.padding(top = 24.dp),
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
                number = 5,
                time = Time(LocalTime.of(16, 20), LocalTime.of(17, 55)),
            ).let { ScheduleDayItem.ClassesEntry(4, it, it.toUiModel(ScheduleType.GROUP)) },
        ),
    ),
    ScheduleDayPage(dayOfWeek = 2, items = listOf(ScheduleDayItem.SelfStudy)),
    ScheduleDayPage(dayOfWeek = 3, items = listOf(ScheduleDayItem.Shimmer)),
)

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            pages = PreviewPages,
            selectedPage = 0,
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
            pages = PreviewPages,
            selectedPage = 0,
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
            pages = PreviewPages,
            selectedPage = 1,
            onAction = {},
            initialPage = 1,
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDaysPagerShimmerPreview() {
    MpeixTheme {
        ScheduleDaysPager(
            pages = PreviewPages,
            selectedPage = 2,
            onAction = {},
            initialPage = 2,
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
