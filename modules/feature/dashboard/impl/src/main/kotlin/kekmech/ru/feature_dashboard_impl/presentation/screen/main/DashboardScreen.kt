package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem.Section
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.DayStatus
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.FavoriteScheduleItem
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.LunchBanner
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.SearchField
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.SelectedScheduleItem
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.TimePrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose.UpcomingEventsShimmer
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_ui.NoteItem
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_ui.compose.ClassesItem
import kekmech.ru.lib_elm.toResource
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_states.EmptyState
import kekmech.ru.ui_kit_states.ErrorState
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration.Companion.minutes

// Bottom navigation bar is drawn above the tab content
private val BottomNavigationBarPadding = 80.dp

/**
 * Scroll position of the dashboard list. The back stack is replaced on every tab switch,
 * so it is stored here, to restore it when the user returns to the dashboard.
 */
private object SavedScrollPosition {
    var index: Int = 0
    var offset: Int = 0
}

@Composable
internal fun DashboardScreen(
    state: DashboardState,
    scrollToTopRequests: Flow<Unit>,
    onAction: (DashboardAction) -> Unit,
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val currentOnAction by rememberUpdatedState(onAction)
    val converter = remember(context) { DashboardListConverter(context) }
    val listItems = remember(converter, state) { converter.map(state) }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = SavedScrollPosition.index,
        initialFirstVisibleItemScrollOffset = SavedScrollPosition.offset,
    )
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(scrollToTopRequests) {
        scrollToTopRequests.collect { listState.animateScrollToItem(0) }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { it }
            .collect { currentOnAction(DashboardAction.ListScrolled) }
    }
    DisposableEffect(listState) {
        onDispose { listState.saveScrollPosition() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MpeixTheme.palette.surface,
        contentColor = MpeixTheme.palette.onSurface,
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { onAction(DashboardAction.Refresh) },
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isLoading,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding()),
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .adaptiveContentWidth(),
                state = listState,
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    bottom = innerPadding.calculateBottomPadding() + BottomNavigationBarPadding,
                ),
            ) {
                items(listItems, key = { it.key }) { item ->
                    DashboardItem(item, onAction)
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.DashboardItem(
    item: DashboardListItem,
    onAction: (DashboardAction) -> Unit,
) {
    val itemModifier = Modifier.animateItem()
    val cardModifier = itemModifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    when (item) {
        is DashboardListItem.Space -> Spacer(itemModifier.height(item.height))
        is DashboardListItem.DayStatus -> DayStatus(
            header = item.header,
            description = item.description,
            modifier = itemModifier,
        )

        is DashboardListItem.SearchField -> SearchField(
            onClick = { onAction(DashboardAction.SearchClick) },
            modifier = itemModifier.padding(horizontal = 16.dp),
        )

        is DashboardListItem.SelectedSchedule -> SelectedScheduleItem(
            name = item.name,
            type = item.type,
            onClick = { onAction(DashboardAction.ChangeScheduleClick) },
            modifier = itemModifier,
        )

        is DashboardListItem.LunchBanner -> LunchBanner(
            onClick = { onAction(DashboardAction.LunchBannerClick) },
            modifier = itemModifier.padding(horizontal = 16.dp),
        )

        is DashboardListItem.SectionHeader -> SectionHeader(
            title = item.title,
            subtitle = item.subtitle,
            actionText = item.actionText,
            onActionClick = {
                when (item.section) {
                    Section.NOTES -> onAction(DashboardAction.ShowAllNotesClick)
                    Section.FAVORITES -> onAction(DashboardAction.SetUpFavoritesClick)
                    Section.EVENTS -> Unit
                }
            },
            modifier = itemModifier,
        )

        is DashboardListItem.EmptyState -> EmptyState(
            title = item.title,
            subtitle = item.subtitle,
            modifier = itemModifier,
        )

        is DashboardListItem.EventsShimmer -> UpcomingEventsShimmer(itemModifier)
        is DashboardListItem.EventsError -> ErrorState(
            throwable = item.throwable,
            onReloadClick = { onAction(DashboardAction.Reload) },
            modifier = itemModifier.padding(vertical = 12.dp),
        )

        is DashboardListItem.TimePrediction -> TimePrediction(
            text = item.text,
            modifier = itemModifier,
        )

        is DashboardListItem.ClassesEntry -> ClassesItem(
            model = item.model,
            onClick = { onAction(DashboardAction.ClassesClick(item.classes)) },
            modifier = cardModifier,
        )

        is DashboardListItem.NoteEntry -> NoteItem(
            content = item.note.content,
            date = item.note.dateTime.toLocalDate(),
            disciplineName = item.note.classesName,
            onClick = { onAction(DashboardAction.NoteClick(item.note)) },
            modifier = cardModifier,
        )

        is DashboardListItem.FavoriteEntry -> FavoriteScheduleItem(
            name = item.favoriteSchedule.name,
            description = item.favoriteSchedule.description,
            isSelected = item.isSelected,
            onClick = { onAction(DashboardAction.FavoriteScheduleClick(item.favoriteSchedule)) },
            modifier = cardModifier,
        )
    }
}

private fun LazyListState.saveScrollPosition() {
    SavedScrollPosition.index = firstVisibleItemIndex
    SavedScrollPosition.offset = firstVisibleItemScrollOffset
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

private val PreviewState = DashboardState(
    selectedSchedule = SelectedSchedule(name = "С-12-16", type = ScheduleType.GROUP),
    weekOfSemester = WeekOfSemester.Studying(12).toResource(),
    upcomingEvents = UpcomingEventsPrediction.ClassesTodayStarted(
        inProgressClasses = listOf(PreviewClasses),
        inProgressFactor = 0.4f,
        futureClasses = listOf(
            PreviewClasses.copy(
                name = "Технология металлов",
                type = ClassesType.PRACTICE,
                number = 2,
                time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
            ),
            PreviewClasses.copy(
                name = "Электротехническое материаловедение",
                type = ClassesType.LAB,
                place = "Аудитория Е-306",
                groups = "С-12-16\\1п",
                number = 3,
                time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20)),
            ),
            PreviewClasses.copy(
                name = "Электротехническое материаловедение",
                type = ClassesType.LAB,
                place = "Аудитория Е-308",
                groups = "С-12-16\\2п",
                person = "Иванов И.И.",
                number = 3,
                time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20)),
            ),
        ),
    ).toResource(),
    actualNotes = listOf(
        Note(
            content = "Принести типовой расчет №1",
            dateTime = LocalDateTime.of(2026, 11, 4, 11, 10),
            classesName = "Технология металлов",
            target = 0,
            id = 1,
        ),
    ).toResource(),
    favoriteSchedules = listOf(
        FavoriteSchedule(name = "С-12-16", type = ScheduleType.GROUP, description = "Моя группа", order = 0),
        FavoriteSchedule(name = "А-08-19", type = ScheduleType.GROUP, description = "Группа друга", order = 1),
    ).toResource(),
)

@PreviewLightDark
@Composable
private fun DashboardScreenPreview() {
    MpeixPreview {
        DashboardScreen(
            state = PreviewState,
            scrollToTopRequests = emptyFlow(),
            onAction = { /* no-op */ },
        )
    }
}

@Preview
@Composable
private fun DashboardScreenLoadingPreview() {
    MpeixPreview {
        DashboardScreen(
            state = DashboardState(selectedSchedule = PreviewState.selectedSchedule),
            scrollToTopRequests = emptyFlow(),
            onAction = { /* no-op */ },
        )
    }
}

@Preview
@Composable
private fun DashboardScreenInNDaysPreview() {
    MpeixPreview {
        DashboardScreen(
            state = PreviewState.copy(
                upcomingEvents = UpcomingEventsPrediction.ClassesInNDays(
                    date = LocalDate.of(2026, 11, 5),
                    dayOffset = 1,
                    timeLeft = 905.minutes,
                    futureClasses = listOf(PreviewClasses),
                ).toResource(),
                actualNotes = emptyList<Note>().toResource(),
            ),
            scrollToTopRequests = emptyFlow(),
            onAction = { /* no-op */ },
        )
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun DashboardScreenTabletPreview() {
    MpeixPreview {
        DashboardScreen(
            state = PreviewState,
            scrollToTopRequests = emptyFlow(),
            onAction = { /* no-op */ },
        )
    }
}
