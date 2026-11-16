package kekmech.ru.feature_schedule_impl.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDayItem
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDayPage
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDaysAction
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDaysPager
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleNavigationFab
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleWeeksStrip
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

private val HeaderShadowHeight = 8.dp

// The FAB floats above the floating bottom navigation bar
private val FabBottomPadding = 96.dp

/**
 * Actions of the [ScheduleScreen], handled by the `ScheduleEntry`
 */
internal sealed interface ScheduleAction {

    /**
     * The user swiped the week calendar to the week with the [weekOffset]
     */
    data class WeekSelected(val weekOffset: Int) : ScheduleAction

    /**
     * The user started dragging the week calendar
     */
    data object WeeksDragged : ScheduleAction

    data class DayClick(val date: LocalDate) : ScheduleAction
    data object FabClick : ScheduleAction

    /**
     * Action of the days pager
     */
    data class Days(val action: ScheduleDaysAction) : ScheduleAction
}

/**
 * # ScheduleScreen
 *
 * Schedule tab: the selected day and week of the semester in the header, the endless week calendar
 * under it, and the classes of the selected week day by day in the pager below.
 *
 * @param ui what to show, see [ScheduleUiConverter]
 * @param onAction called on the user actions
 * @param modifier [Modifier] to be applied to the screen
 * @param today current day, the week offsets of the [ui] are counted from its week
 */
@Composable
internal fun ScheduleScreen(
    ui: ScheduleUi,
    onAction: (ScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = moscowLocalDate(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MpeixTheme.palette.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .adaptiveContentWidth(),
        ) {
            ScheduleHeader(ui = ui, today = today, onAction = onAction)
            Box(modifier = Modifier.weight(1f)) {
                ScheduleDaysPager(
                    pages = ui.dayPages,
                    selectedPage = ui.selectedDayPage,
                    onAction = { onAction(ScheduleAction.Days(it)) },
                    savedScheduleBannerText = ui.savedScheduleBanner?.let { savedScheduleBannerText(it) },
                    // The store outlives the screen, so the pager opens on the selected day right away
                    initialPage = ui.selectedDayPage.coerceIn(0, (ui.dayPages.size - 1).coerceAtLeast(0)),
                )
                HeaderShadow()
            }
        }
        ui.fab?.let { fab ->
            ScheduleNavigationFab(
                isNextWeek = fab.isNextWeek,
                isVisible = fab.isVisible,
                onClick = { onAction(ScheduleAction.FabClick) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = FabBottomPadding),
            )
        }
    }
}

@Composable
private fun ScheduleHeader(
    ui: ScheduleUi,
    today: LocalDate,
    onAction: (ScheduleAction) -> Unit,
) {
    val daysOfWeek = stringArrayResource(StringArrays.days_of_week)
    val months = stringArrayResource(StringArrays.months)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surface)
            .statusBarsPadding(),
    ) {
        Text(
            text = remember(ui.selectedDate, daysOfWeek, months) {
                ScheduleHeaderFormatter.formatDay(ui.selectedDate, daysOfWeek, months)
            },
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        Text(
            text = ui.weekOfSemester?.let { weekOfSemesterText(it) }.orEmpty(),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
        )
        ScheduleWeeksStrip(
            weekOffset = ui.weekOffset,
            selectedDate = ui.selectedDate,
            onWeekSelect = { onAction(ScheduleAction.WeekSelected(it)) },
            onDayClick = { onAction(ScheduleAction.DayClick(it)) },
            onScroll = { onAction(ScheduleAction.WeeksDragged) },
            today = today,
        )
    }
}

/**
 * Shadow under the header, the classes scroll under it
 */
@Composable
private fun HeaderShadow() {
    val surface = MpeixTheme.palette.surface
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeaderShadowHeight)
            .background(Brush.verticalGradient(listOf(surface, Color.Transparent))),
    )
}

@Composable
private fun weekOfSemesterText(weekOfSemester: WeekOfSemester): String =
    when (weekOfSemester) {
        is WeekOfSemester.Studying -> stringResource(Strings.schedule_semester_week, weekOfSemester.num)
        is WeekOfSemester.NonStudying -> stringResource(Strings.schedule_weekend_week)
    }

@Composable
private fun savedScheduleBannerText(banner: SavedScheduleBannerUi): String {
    val locale = LocalConfiguration.current.locales[0]
    val updatedAt = remember(banner.updatedAt, locale) {
        banner.updatedAt?.let { ScheduleHeaderFormatter.formatUpdatedAt(it, locale) }
    }
    return stringResource(Strings.schedule_saved_banner_text, updatedAt.orEmpty())
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

private val PreviewUi = ScheduleUi(
    selectedDate = LocalDate.of(2026, 11, 13),
    weekOffset = 0,
    weekOfSemester = WeekOfSemester.Studying(12),
    dayPages = List(6) { index ->
        ScheduleDayPage(
            dayOfWeek = index + 1,
            items = listOf(
                ScheduleDayItem.ClassesEntry(0, PreviewClasses, PreviewClasses.toUiModel(ScheduleType.GROUP)),
                ScheduleDayItem.Lunch,
            ),
        )
    },
    selectedDayPage = 4,
    savedScheduleBanner = null,
    fab = ScheduleFabUi(isNextWeek = true, isVisible = true),
)

@PreviewLightDark
@Composable
private fun ScheduleScreenPreview() {
    MpeixPreview {
        ScheduleScreen(ui = PreviewUi, onAction = {}, today = PreviewUi.selectedDate)
    }
}

@PreviewLightDark
@Composable
private fun ScheduleScreenSavedSchedulePreview() {
    MpeixPreview {
        ScheduleScreen(
            ui = PreviewUi.copy(
                selectedDate = PreviewUi.selectedDate.plusWeeks(2),
                weekOffset = 2,
                weekOfSemester = WeekOfSemester.NonStudying,
                savedScheduleBanner = SavedScheduleBannerUi(updatedAt = Instant.parse("2026-11-10T11:20:00Z")),
                fab = ScheduleFabUi(isNextWeek = false, isVisible = true),
            ),
            onAction = {},
            today = PreviewUi.selectedDate,
        )
    }
}
