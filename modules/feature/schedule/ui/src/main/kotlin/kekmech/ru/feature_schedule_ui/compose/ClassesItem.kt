package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_ui.utils.classesTypeName
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kekmech.ru.res_strings.R.array as StringArrays

private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val TimeColumnWidth = 80.dp
private val ItemShape = RoundedCornerShape(12.dp)
private val ChipShape = RoundedCornerShape(12.dp)
private val NoteShape = RoundedCornerShape(topStart = 3.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)

/**
 * # ClassesItem
 *
 * Card of the single classes in the schedule: time, number, type, name, place and person/groups.
 * Optionally shows the [ClassesUiModel.notePreview] at the bottom and the animated
 * [ClassesUiModel.progress] background of the ongoing classes.
 *
 * Replaces the `ClassesAdapterItem` + `NotePreviewAdapterItem` + `ProgressBackgroundDrawable`
 * combination of the RecyclerView implementation.
 *
 * Usage example:
 * ```kotlin
 * ClassesItem(
 *     model = classes.toUiModel(scheduleType),
 *     onClick = { onClassesClick(classes) },
 * )
 * ```
 *
 * @param model the classes to show, see [toUiModel]
 * @param modifier [Modifier] to be applied to the card
 * @param onClick called when the card is clicked, or null if the card is not clickable
 */
@Composable
public fun ClassesItem(
    model: ClassesUiModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val progress = model.progress
    val notePreview = model.notePreview
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(ItemShape)
            .background(MpeixTheme.palette.secondary)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        if (progress != null) {
            WaveProgressBackground(
                progress = progress,
                color = MpeixTheme.palette.primary,
                modifier = Modifier.matchParentSize(),
            )
        }
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Row {
                ClassesTime(
                    model = model,
                    modifier = Modifier
                        .width(TimeColumnWidth)
                        .padding(top = 32.dp, end = 16.dp),
                )
                ClassesInfo(
                    model = model,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp, end = 24.dp),
                )
            }
            if (!notePreview.isNullOrBlank()) {
                NotePreview(
                    text = notePreview,
                    modifier = Modifier.padding(start = TimeColumnWidth, end = 16.dp, top = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun ClassesTime(
    model: ClassesUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = model.startTime.format(TimeFormatter),
            style = MpeixTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = model.endTime.format(TimeFormatter),
            style = MpeixTheme.typography.bodyMedium,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@Composable
private fun ClassesInfo(
    model: ClassesUiModel,
    modifier: Modifier = Modifier,
) {
    val classesNumbers = stringArrayResource(StringArrays.classes_numbers)
    val classesTypes = stringArrayResource(StringArrays.classes_types)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(
                text = classesNumbers.getOrElse(model.number) { classesNumbers[0] },
                containerColor = MpeixTheme.palette.outlineVariant,
                contentColor = MpeixTheme.palette.onSurface,
            )
            val (containerColor, contentColor) = model.type.chipColors()
            Chip(
                text = classesTypeName(model.type, model.rawType, classesTypes),
                containerColor = containerColor,
                contentColor = contentColor,
            )
        }
        Text(
            text = model.name,
            style = MpeixTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = model.place,
            style = MpeixTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
        if (model.secondaryLabel != null) {
            Text(
                text = model.secondaryLabel,
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun Chip(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Text(
        text = text.uppercase(),
        style = MpeixTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = contentColor,
        maxLines = 1,
        modifier = Modifier
            .background(containerColor, ChipShape)
            .padding(horizontal = 8.dp, vertical = 1.dp),
    )
}

@Composable
private fun ClassesType.chipColors(): Pair<Color, Color> =
    when (this) {
        ClassesType.LECTURE -> MpeixTheme.palette.markGreen to MpeixTheme.palette.onMarkGreen
        ClassesType.PRACTICE -> MpeixTheme.palette.markYellow to MpeixTheme.palette.onMarkYellow
        ClassesType.LAB -> MpeixTheme.palette.markRed to MpeixTheme.palette.onMarkRed
        else -> MpeixTheme.palette.outlineVariant to MpeixTheme.palette.onSurface
    }

@Composable
private fun NotePreview(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = 32.dp)
            .background(MpeixTheme.palette.surfaceContainerHighest, NoteShape)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            style = MpeixTheme.typography.bodyMedium,
            fontSize = 13.sp,
            color = MpeixTheme.palette.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@PreviewLightDark
@Composable
private fun ClassesItemPreview() {
    val model = ClassesUiModel(
        name = "Инженерная и компьютерная графика",
        type = ClassesType.LECTURE,
        rawType = null,
        number = 1,
        place = "Аудитория Б-407",
        secondaryLabel = "Чахеев Е.Я.",
        startTime = LocalTime.of(9, 20),
        endTime = LocalTime.of(10, 55),
    )
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ClassesItem(model = model, onClick = {})
            ClassesItem(
                model = model.copy(
                    name = "Технология металлов",
                    type = ClassesType.PRACTICE,
                    number = 2,
                    secondaryLabel = "Чахеев Е.Я.\nА-08-19\\1п",
                    notePreview = "Принести типовой расчет №1 и все-все остальные расчеты",
                ),
            )
            ClassesItem(
                model = model.copy(type = ClassesType.LAB, number = 3, progress = 0.5f),
                onClick = { /* no-op */ },
            )
            ClassesItem(
                model = model.copy(
                    type = ClassesType.UNDEFINED,
                    rawType = "Зачёт",
                    number = 4,
                    secondaryLabel = "А-08-19, А-12-19",
                    notePreview = "Не забыть зачётку",
                    progress = 0.8f,
                ),
            )
        }
    }
}
