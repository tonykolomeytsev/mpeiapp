package kekmech.ru.feature_search_impl.screens.schedule_details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.compose.ClassesItem
import kekmech.ru.feature_schedule_ui.compose.DayChip
import kekmech.ru.feature_schedule_ui.compose.WORKING_DAYS_IN_WEEK
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.feature_search_impl.R
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_kit_dialogs.BottomSheet
import kekmech.ru.ui_kit_states.EmptyState
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

private val HorizontalPadding = 16.dp
private val IconSize = 18.dp
private val DayChipWidth = 64.dp
private val PlaceholderShape = RoundedCornerShape(8.dp)
private val ClassesPlaceholderHeight = 128.dp
private val DayPlaceholderWidth = 44.dp
private val DayPlaceholderHeight = 64.dp
private val RowPlaceholderIconSize = 20.dp
private val RowPlaceholderTextWidth = 250.dp

/**
 * Actions of the [ScheduleDetailsScreen], handled by the [ScheduleDetailsEntry]
 */
internal sealed interface ScheduleDetailsAction {
    data object FavoritesClick : ScheduleDetailsAction
    data class DayClick(val date: LocalDate) : ScheduleDetailsAction
    data object SwitchScheduleClick : ScheduleDetailsAction
}

/**
 * # ScheduleDetailsScreen
 *
 * Bottom sheet with the schedule found by the search: this and next weeks,
 * classes of the selected day and buttons to add the schedule to favorites or switch to it.
 *
 * The sheet always takes the full height below the status bar,
 * so it doesn't jump when the schedule loads or another day is selected.
 *
 * @param ui content of the sheet, see [ScheduleDetailsUiConverter]
 * @param onAction called on the user actions
 * @param modifier [Modifier] to be applied to the sheet
 */
@Composable
internal fun ScheduleDetailsScreen(
    ui: ScheduleDetailsUi,
    onAction: (ScheduleDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    BottomSheet(
        modifier = modifier.fillMaxHeightBelow(statusBarHeight),
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
        ) {
            item(key = "header") {
                Header(name = ui.name, description = ui.description)
            }
            item(key = "favorites") {
                if (ui.isInFavorites == null) {
                    RowPlaceholder()
                } else {
                    FavoritesRow(
                        isInFavorites = ui.isInFavorites,
                        onClick = { onAction(ScheduleDetailsAction.FavoritesClick) },
                    )
                }
            }
            item(key = "weeks_summary") {
                if (ui.weeks == null) {
                    RowPlaceholder()
                } else {
                    IconTextRow(
                        iconRes = Icons.ic_navigation_schedule,
                        iconTint = MpeixTheme.palette.onSurfaceVariant,
                        text = weeksSummary(ui.weeks),
                        textStyle = MpeixTheme.typography.bodyMedium,
                        textColor = MpeixTheme.palette.onSurface,
                    )
                }
            }
            item(key = "days") {
                if (ui.weeks == null) {
                    DaysPlaceholder(Modifier.padding(vertical = 8.dp))
                } else {
                    Days(
                        days = ui.weeks.days,
                        onDayClick = { onAction(ScheduleDetailsAction.DayClick(it)) },
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
            classes(ui.weeks)
            item(key = "switch_schedule") {
                Button(
                    text = stringResource(Strings.search_schedule_details_switch_schedule),
                    onClick = { onAction(ScheduleDetailsAction.SwitchScheduleClick) },
                    palette = ButtonPalette.Secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = HorizontalPadding, end = HorizontalPadding, top = 8.dp),
                )
            }
        }
    }
}

/**
 * Takes the whole available height except the [topGap].
 *
 * `statusBarsPadding()` can't be used here: the modal sheet consumes the status bar insets by its
 * offset, so near the top the padding changes on every frame of the drag. The sheet edge then
 * stands still while the content shrinks, and the sheet feels stuck under the finger.
 */
private fun Modifier.fillMaxHeightBelow(topGap: Dp): Modifier = layout { measurable, constraints ->
    val placeable = if (constraints.hasBoundedHeight) {
        val height = (constraints.maxHeight - topGap.roundToPx())
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        measurable.measure(constraints.copy(minHeight = height, maxHeight = height))
    } else {
        measurable.measure(constraints)
    }
    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

@Composable
private fun Header(name: String, description: String) {
    Column(modifier = Modifier.padding(start = HorizontalPadding, end = HorizontalPadding, bottom = 8.dp)) {
        Text(
            text = name,
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = description,
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
private fun FavoritesRow(isInFavorites: Boolean, onClick: () -> Unit) {
    val (textRes, iconRes) = if (isInFavorites) {
        Strings.search_schedule_details_remove_favorites to R.drawable.ic_star_black_18dp
    } else {
        Strings.search_schedule_details_add_favorites to R.drawable.ic_star_outline_black_18dp
    }
    IconTextRow(
        iconRes = iconRes,
        iconTint = MpeixTheme.palette.primary,
        text = AnnotatedString(stringResource(textRes)),
        textStyle = MpeixTheme.typography.titleSmall,
        textColor = MpeixTheme.palette.primary,
        onClick = onClick,
    )
}

/**
 * Icon followed by the text, the icon is centered on the first line of the text:
 * a single line row is centered, and the icon stays next to the first line when the text wraps.
 *
 * A `Row` can't do both: Compose trims the line height above the first line
 * (`LineHeightStyle.Trim.Both`), so a top aligned icon sits lower than a single line text,
 * and a centered icon drifts between the lines of a wrapped text.
 */
@Composable
private fun IconTextRow(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    text: AnnotatedString,
    textStyle: TextStyle,
    textColor: Color,
    onClick: (() -> Unit)? = null,
) {
    // The text writes its layout while it's measured, the icon is placed by it in the same layout pass
    val textLayout = remember { TextLayoutHolder() }
    Layout(
        content = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(IconSize),
            )
            Text(
                text = text,
                style = textStyle,
                color = textColor,
                onTextLayout = { textLayout.result = it },
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = HorizontalPadding, vertical = 6.dp),
    ) { measurables, constraints ->
        val gap = HorizontalPadding.roundToPx()
        val icon = measurables[0].measure(Constraints())
        val textMaxWidth = (constraints.maxWidth - icon.width - gap).coerceAtLeast(0)
        val text = measurables[1].measure(Constraints(maxWidth = textMaxWidth))
        val firstLineCenter = textLayout.result
            ?.takeIf { it.lineCount > 0 }
            ?.let { (it.getLineTop(0) + it.getLineBottom(0)) / 2f }
            ?: (text.height / 2f)
        val iconTop = (firstLineCenter - icon.height / 2f).roundToInt()
        // The icon is taller than a single line, so it may stick out above the text: move both down
        val contentTop = (-iconTop).coerceAtLeast(0)
        val height = maxOf(contentTop + text.height, contentTop + iconTop + icon.height)
            .coerceAtLeast(constraints.minHeight)
        layout(constraints.maxWidth, height) {
            icon.placeRelative(0, contentTop + iconTop)
            text.placeRelative(icon.width + gap, contentTop)
        }
    }
}

private class TextLayoutHolder {
    var result: TextLayoutResult? = null
}

/**
 * "12 пар на этой неделе, нет пар на следующей." with the classes counts in bold
 */
@Composable
private fun weeksSummary(weeks: ScheduleDetailsWeeksUi): AnnotatedString {
    val thisWeek = weekSummary(
        classesCount = weeks.thisWeekClassesCount,
        descriptionRes = Strings.search_schedule_details_this_week_description,
        emptyDescriptionRes = Strings.search_schedule_details_this_week_description_empty,
    )
    val nextWeek = weekSummary(
        classesCount = weeks.nextWeekClassesCount,
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
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), countStart, countStart + count.length)
        }
    }
}

@Composable
private fun Days(
    days: List<ScheduleDetailsDayUi>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = remember { moscowLocalDate() }
    LazyRow(
        modifier = modifier.padding(horizontal = HorizontalPadding),
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        items(days, key = { it.date.toEpochDay() }) { day ->
            DayChip(
                date = day.date,
                isSelected = day.isSelected,
                isToday = day.date == today,
                onClick = { onDayClick(day.date) },
                modifier = Modifier.width(DayChipWidth),
            )
        }
    }
}

private fun LazyListScope.classes(weeks: ScheduleDetailsWeeksUi?) {
    when {
        weeks == null -> item(key = "classes_placeholder") {
            ClassesPlaceholder()
        }

        weeks.selectedDayClasses.isEmpty() -> item(key = "classes_empty") {
            EmptyState(title = stringResource(Strings.search_schedule_details_empty_title))
        }

        else -> items(weeks.selectedDayClasses) { classes ->
            ClassesItem(
                model = classes,
                modifier = Modifier.padding(start = HorizontalPadding, end = HorizontalPadding, bottom = 8.dp),
            )
        }
    }
}

@Composable
private fun RowPlaceholder() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer()
            .padding(horizontal = HorizontalPadding, vertical = 6.dp),
    ) {
        Box(
            Modifier
                .size(RowPlaceholderIconSize)
                .background(MpeixTheme.palette.outlineVariant, PlaceholderShape)
        )
        Box(
            Modifier
                .padding(start = HorizontalPadding)
                .width(RowPlaceholderTextWidth)
                .height(RowPlaceholderIconSize)
                .background(MpeixTheme.palette.outlineVariant, PlaceholderShape)
        )
    }
}

/**
 * Placeholders are laid out exactly like the [DayChip]s of the loaded [Days]
 */
@Composable
private fun DaysPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shimmer()
            .padding(horizontal = HorizontalPadding + 8.dp),
    ) {
        repeat(WORKING_DAYS_IN_WEEK) {
            Box(
                modifier = Modifier.width(DayChipWidth),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    Modifier
                        .padding(bottom = 8.dp)
                        .width(DayPlaceholderWidth)
                        .height(DayPlaceholderHeight)
                        .background(MpeixTheme.palette.outlineVariant, PlaceholderShape)
                )
            }
        }
    }
}

@Composable
private fun ClassesPlaceholder() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .shimmer()
            .padding(start = HorizontalPadding, end = HorizontalPadding, bottom = 8.dp)
            .height(ClassesPlaceholderHeight)
            .background(MpeixTheme.palette.secondary, PlaceholderShape)
    )
}

private val PreviewMonday = LocalDate.of(2026, 11, 9)

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

private val PreviewUi = ScheduleDetailsUi(
    name = "Зуев Юрий Юрьевич",
    description = "Кафедра электротехники",
    isInFavorites = false,
    weeks = ScheduleDetailsWeeksUi(
        thisWeekClassesCount = 12,
        nextWeekClassesCount = 0,
        days = List(14) { ScheduleDetailsDayUi(PreviewMonday.plusDays(it.toLong()), isSelected = it == 1) },
        selectedDayClasses = listOf(
            PreviewClasses.toUiModel(ScheduleType.PERSON),
            PreviewClasses.copy(
                type = ClassesType.PRACTICE,
                number = 2,
                time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
            ).toUiModel(ScheduleType.PERSON),
        ),
    ),
)

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(ui = PreviewUi, onAction = {}, modifier = Modifier.height(720.dp))
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenEmptyDayPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(
            ui = PreviewUi.copy(
                isInFavorites = true,
                weeks = PreviewUi.weeks?.copy(selectedDayClasses = emptyList()),
            ),
            onAction = {},
            modifier = Modifier.height(720.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDetailsScreenLoadingPreview() {
    MpeixPreview {
        ScheduleDetailsScreen(
            ui = PreviewUi.copy(isInFavorites = null, weeks = null),
            onAction = {},
            modifier = Modifier.height(720.dp),
        )
    }
}
