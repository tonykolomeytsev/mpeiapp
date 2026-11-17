package kekmech.ru.feature_search_impl.screens.schedule_details

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayItem
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPage
import kekmech.ru.feature_schedule_ui.compose.ScheduleDaysAction
import kekmech.ru.feature_schedule_ui.compose.ScheduleDaysPager
import kekmech.ru.feature_schedule_ui.compose.ScheduleWeeksStrip
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.feature_search_impl.R
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import java.time.LocalTime
import kekmech.ru.res_strings.R.string as Strings

private val HorizontalPadding = 16.dp
private val HeaderShadowHeight = 8.dp

private const val FAB_ANIMATION_DURATION_MS = 100
private val FabHeight = 48.dp
private val FabIconSize = 24.dp
private val FabBottomPadding = 16.dp

// Space for the FAB under the classes
private val DaysBottomPadding = 80.dp

private val PlaceholderShape = RoundedCornerShape(8.dp)
private val SummaryPlaceholderWidth = 250.dp
private val SummaryPlaceholderHeight = 16.dp

// The placeholder is centered on the line of the summary text, so the header doesn't jump on load
private val SummaryPlaceholderVerticalPadding = 2.dp

/**
 * Actions of the [ScheduleDetailsScreen], handled by the [ScheduleDetailsEntry]
 */
internal sealed interface ScheduleDetailsAction {
    data object BackClick : ScheduleDetailsAction
    data object FavoritesClick : ScheduleDetailsAction

    /**
     * The user swiped the week calendar to the week with the [weekOffset]
     */
    data class WeekSelected(val weekOffset: Int) : ScheduleDetailsAction

    /**
     * The user started dragging the week calendar
     */
    data object WeeksDragged : ScheduleDetailsAction

    data class DayClick(val date: LocalDate) : ScheduleDetailsAction

    /**
     * Action of the days pager
     */
    data class Days(val action: ScheduleDaysAction) : ScheduleDetailsAction
}

/**
 * # ScheduleDetailsScreen
 *
 * Schedule found by the search, browsed like the Schedule tab: the name in the top app bar,
 * the classes summary of this and next weeks under it, the endless week calendar below,
 * and the classes of the selected week day by day in the pager at the bottom.
 * The FAB adds the schedule to favorites or removes it from them.
 *
 * @param ui what to show, see [ScheduleDetailsUiConverter]
 * @param onAction called on the user actions
 * @param modifier [Modifier] to be applied to the screen
 * @param today current day, the week offsets of the [ui] are counted from its week
 */
@Composable
internal fun ScheduleDetailsScreen(
    ui: ScheduleDetailsUi,
    onAction: (ScheduleDetailsAction) -> Unit,
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
            ScheduleDetailsHeader(ui = ui, today = today, onAction = onAction)
            Box(modifier = Modifier.weight(1f)) {
                ScheduleDaysPager(
                    weeks = ui.dayPages,
                    weekOffset = ui.weekOffset,
                    selectedDayOfWeek = ui.selectedDate.dayOfWeek.value,
                    onAction = { onAction(ScheduleDetailsAction.Days(it)) },
                    // Notes are attached to the selected schedule only, the found one has nothing to open
                    isClassesClickable = false,
                    bottomPadding = DaysBottomPadding,
                )
                HeaderShadow()
            }
        }
        ui.favoritesFab?.let { fab ->
            FavoritesFab(
                isInFavorites = fab.isInFavorites,
                isVisible = fab.isVisible,
                onClick = { onAction(ScheduleDetailsAction.FavoritesClick) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = FabBottomPadding),
            )
        }
    }
}

@Composable
private fun ScheduleDetailsHeader(
    ui: ScheduleDetailsUi,
    today: LocalDate,
    onAction: (ScheduleDetailsAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surface),
    ) {
        TopAppBar(
            navigationIcon = {
                BackIconButton { onAction(ScheduleDetailsAction.BackClick) }
            },
            title = "Быстрый просмотр",
        )
        WeeksSummary(
            title = ui.name,
            summary = ui.weeksSummary,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
        )
        ScheduleWeeksStrip(
            weekOffset = ui.weekOffset,
            selectedDate = ui.selectedDate,
            onWeekSelect = { onAction(ScheduleDetailsAction.WeekSelected(it)) },
            onDayClick = { onAction(ScheduleDetailsAction.DayClick(it)) },
            onScroll = { onAction(ScheduleDetailsAction.WeeksDragged) },
            today = today,
        )
    }
}

@Composable
private fun WeeksSummary(
    title: String,
    summary: ScheduleDetailsWeeksSummaryUi?,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = title,
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        if (summary == null) {
            Box(
                modifier = Modifier
                    .shimmer()
                    .padding(vertical = SummaryPlaceholderVerticalPadding)
                    .size(SummaryPlaceholderWidth, SummaryPlaceholderHeight)
                    .background(MpeixTheme.palette.outlineVariant, PlaceholderShape),
            )
        } else {
            Text(
                text = weeksSummary(summary),
                style = MpeixTheme.typography.titleSmall,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
    }
}

/**
 * "12 пар на этой неделе, нет пар на следующей." with the classes counts in bold
 */
@Composable
private fun weeksSummary(summary: ScheduleDetailsWeeksSummaryUi): AnnotatedString {
    val thisWeek = weekSummary(
        classesCount = summary.thisWeekClassesCount,
        descriptionRes = Strings.search_schedule_details_this_week_description,
        emptyDescriptionRes = Strings.search_schedule_details_this_week_description_empty,
    )
    val nextWeek = weekSummary(
        classesCount = summary.nextWeekClassesCount,
        descriptionRes = Strings.search_schedule_details_next_week_description,
        emptyDescriptionRes = Strings.search_schedule_details_next_week_description_empty,
    )
    return buildAnnotatedString {
        append(thisWeek)
        append(", ")
        append(nextWeek)
    }
}

@Composable
private fun weekSummary(
    classesCount: Int,
    @StringRes descriptionRes: Int,
    @StringRes emptyDescriptionRes: Int,
): AnnotatedString {
    if (classesCount <= 0) return AnnotatedString(stringResource(emptyDescriptionRes))
    val countFormRes = when (ScheduleDetailsUiConverter.classesCountForm(classesCount)) {
        ClassesCountForm.One -> Strings.search_schedule_details_classes_1
        ClassesCountForm.Few -> Strings.search_schedule_details_classes_2_4
        ClassesCountForm.Many -> Strings.search_schedule_details_classes_5_0
    }
    val count = "$classesCount ${stringResource(countFormRes)}"
    val description = stringResource(descriptionRes, count)
    return buildAnnotatedString {
        append(description)
        val countStart = description.indexOf(count)
        if (countStart >= 0) {
            addStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.onSurface,
                ),
                countStart,
                countStart + count.length
            )
        }
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

/**
 * Adds the schedule to favorites or removes it from them, styled like the navigation FAB of the Schedule tab.
 *
 * @param isVisible the button fades out when false and doesn't react to clicks while hidden
 */
@Composable
private fun FavoritesFab(
    isInFavorites: Boolean,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(FAB_ANIMATION_DURATION_MS),
        label = "FavoritesFabAlpha",
    )
    val (textRes, iconRes) = if (isInFavorites) {
        Strings.search_schedule_details_remove_favorites to R.drawable.ic_star_black_18dp
    } else {
        Strings.search_schedule_details_add_favorites to R.drawable.ic_star_outline_black_18dp
    }
    ExtendedFloatingActionButton(
        text = {
            Text(
                text = stringResource(textRes).uppercase(),
                style = MpeixTheme.typography.titleSmall,
                maxLines = 1,
            )
        },
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(FabIconSize),
            )
        },
        // The hidden button stays in the layout, so it keeps its place and just ignores the clicks
        onClick = { if (isVisible) onClick() },
        modifier = modifier
            .height(FabHeight)
            .alpha(alpha)
            .then(if (isVisible) Modifier else Modifier.semantics { hideFromAccessibility() }),
        containerColor = MpeixTheme.palette.secondary,
        contentColor = MpeixTheme.palette.onSecondary,
        shape = CircleShape,
    )
}

private val PreviewToday = LocalDate.of(2026, 11, 10)

private val PreviewClasses = Classes(
    name = "Электротехника",
    type = ClassesType.LECTURE,
    rawType = null,
    place = "Аудитория А-300",
    groups = "С-12-16",
    person = "Зуев Юрий Юрьевич",
    time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)),
    number = 1,
)

private val PreviewDayPages = List(6) { index ->
    val practice = PreviewClasses.copy(
        type = ClassesType.PRACTICE,
        number = 2,
        time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
    )
    ScheduleDayPage(
        dayOfWeek = index + 1,
        items = listOf(
            ScheduleDayItem.ClassesEntry(
                0,
                PreviewClasses,
                PreviewClasses.toUiModel(ScheduleType.PERSON)
            ),
            ScheduleDayItem.ClassesEntry(1, practice, practice.toUiModel(ScheduleType.PERSON)),
            ScheduleDayItem.Lunch,
        ),
    )
}

private val PreviewUi = ScheduleDetailsUi(
    name = "Зуев Юрий Юрьевич",
    weeksSummary = ScheduleDetailsWeeksSummaryUi(
        thisWeekClassesCount = 12,
        nextWeekClassesCount = 0
    ),
    selectedDate = PreviewToday,
    weekOffset = 0,
    dayPages = mapOf(0 to PreviewDayPages),
    favoritesFab = ScheduleDetailsFavoritesFabUi(isInFavorites = false, isVisible = true),
)

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(ui = PreviewUi, onAction = {}, today = PreviewToday)
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenInFavoritesPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(
            ui = PreviewUi.copy(
                favoritesFab = ScheduleDetailsFavoritesFabUi(
                    isInFavorites = true,
                    isVisible = true
                ),
            ),
            onAction = {},
            today = PreviewToday,
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenLoadingPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(
            ui = PreviewUi.copy(weeksSummary = null, dayPages = emptyMap(), favoritesFab = null),
            onAction = {},
            today = PreviewToday,
        )
    }
}
