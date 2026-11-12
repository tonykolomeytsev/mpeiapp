package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import kekmech.ru.feature_schedule_ui.compose.icon.ArrowSplit
import kekmech.ru.feature_schedule_ui.utils.classesTypeName
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kekmech.ru.res_strings.R.array as StringArrays

private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val TimeColumnWidth = 76.dp
private val ItemShape = RoundedCornerShape(12.dp)
private val ChipShape = RoundedCornerShape(12.dp)
private val NoteShape =
    RoundedCornerShape(topStart = 3.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
private val LectureTagColor = Color(0xFF578B0F)
private val PracticeTagColor = Color(0xFFFFC954)
private val LabTagColor = Color(0xFFDB4437)

/** Size of the type dot and the subgroup icon at the start of the lines */
private val MarkerSize = 16.dp
private val MarkerSpacing = 8.dp

/** Spacing between the lines of the place, teacher and group */
private val MetaLineSpacing = 2.dp

/**
 * # ClassesItem
 *
 * Card of the classes in the schedule: time, number, type, name, place, teacher and group.
 * Optionally shows the [ParallelClassesUi.notePreview] at the bottom and the animated
 * [ParallelClassesUi.progress] background of the ongoing classes.
 *
 * Several parallel classes with the same name are shown as a single card. The layout depends
 * on how much the classes have in common, see [ParallelClassesUi]:
 * - [ParallelClassesUi.Single] — type, name and place, like a regular classes;
 * - [ParallelClassesUi.SameNameAndType] — common type and name, then a block per subgroup;
 * - [ParallelClassesUi.SameNameDifferentTypes] — common name, then a block per type.
 *
 * Replaces the `ClassesAdapterItem` + `NotePreviewAdapterItem` + `ProgressBackgroundDrawable`
 * combination of the RecyclerView implementation.
 *
 * Usage example:
 * ```kotlin
 * day.classes.groupParallelClasses().forEach { parallelClasses ->
 *     ClassesItem(
 *         model = parallelClasses.toUiModel(scheduleType),
 *         onClick = { onClassesClick(parallelClasses.first()) },
 *     )
 * }
 * ```
 *
 * @param model the classes to show, see [toUiModel]
 * @param modifier [Modifier] to be applied to the card
 * @param onClick called when the card is clicked, or null if the card is not clickable
 */
@Composable
public fun ClassesItem(
    model: ParallelClassesUi,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    ClassesCard(
        model = model,
        onClick = onClick,
        modifier = modifier,
    ) {
        when (model) {
            is ParallelClassesUi.Single -> SingleContent(model)
            is ParallelClassesUi.SameNameAndType -> SameNameAndTypeContent(model)
            is ParallelClassesUi.SameNameDifferentTypes -> SameNameDifferentTypesContent(model)
        }
    }
}

/**
 * Frame of the card, common for all the layouts: background, progress, time and note preview.
 * The [content] is placed to the right of the time.
 */
@Composable
private fun ClassesCard(
    model: ParallelClassesUi,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
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
        val hasNotePreview = !notePreview.isNullOrBlank()
        Column(modifier = Modifier.padding(bottom = if (hasNotePreview) 8.dp else 16.dp)) {
            Row {
                ClassesTime(
                    time = model.time,
                    modifier = Modifier
                        .width(TimeColumnWidth)
                        .padding(top = 32.dp, end = 16.dp),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = content,
                )
            }
            if (hasNotePreview) {
                NotePreview(
                    text = notePreview,
                    modifier = Modifier.padding(start = TimeColumnWidth, end = 16.dp, top = 12.dp),
                )
            }
        }
    }
}

/**
 * The simplest layout:
 * ```
 * [1 ПАРА] ● ЛЕКЦИЯ
 * Name
 * Place
 * Teacher
 * Group
 * ```
 */
@Composable
private fun SingleContent(model: ParallelClassesUi.Single) {
    ClassesHeader(number = model.number, type = model.type, rawType = model.rawType)
    ClassesName(name = model.name)
    MetaInfo(place = model.place, teacher = model.teacher, group = model.group)
}

/**
 * Same as [SingleContent], but every subgroup gets its own block. If all the subgroups
 * are in the same place, the place is shown only once above the blocks:
 * ```
 * [3 ПАРА] ● ЛАБ. РАБОТА
 * Name
 * ⑂ Place 1
 *   Teacher 1
 *   Group 1
 * ⑂ Place 2
 *   Teacher 2
 *   Group 2
 * ```
 */
@Composable
private fun SameNameAndTypeContent(model: ParallelClassesUi.SameNameAndType) {
    ClassesHeader(number = model.number, type = model.type, rawType = model.rawType)
    ClassesName(name = model.name)
    val commonPlace = model.differentMetaInfo.commonPlace(ClassesMinimalMetaUi::place)
    if (commonPlace != null) PlaceText(place = commonPlace)
    ParallelBlocks {
        for (meta in model.differentMetaInfo) {
            val place = meta.place.takeIf { commonPlace == null }
            if (place == null && meta.teacher == null && meta.group == null) continue
            SubgroupBlock {
                MetaInfo(place = place, teacher = meta.teacher, group = meta.group)
            }
        }
    }
}

/**
 * The name is common, but the type moves from the header into the blocks. If all the classes
 * are in the same place, the place is shown only once above the blocks:
 * ```
 * [2 ПАРА]
 * Name
 * ● ЛЕКЦИЯ
 *   Place 1
 *   Teacher 1
 * ● ПРАКТИКА
 *   Place 2
 *   Teacher 2
 * ```
 */
@Composable
private fun SameNameDifferentTypesContent(model: ParallelClassesUi.SameNameDifferentTypes) {
    ClassesHeader(number = model.number)
    ClassesName(name = model.name)
    val commonPlace = model.differentMetaInfo.commonPlace(ClassesExtendedMetaUi::place)
    if (commonPlace != null) PlaceText(place = commonPlace)
    ParallelBlocks {
        for (meta in model.differentMetaInfo) {
            TypedBlock(type = meta.type, rawType = meta.rawType) {
                MetaInfo(
                    place = meta.place.takeIf { commonPlace == null },
                    teacher = meta.teacher,
                    group = meta.group,
                )
            }
        }
    }
}

@Composable
private fun ClassesTime(
    time: ClassesTimeUi,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = time.startTime.format(TimeFormatter),
            style = MpeixTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = time.endTime.format(TimeFormatter),
            style = MpeixTheme.typography.bodyMedium,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

/**
 * Number chip of the classes, followed by the [TypeLabel] if all the classes have the same [type].
 */
@Composable
private fun ClassesHeader(
    number: Int,
    type: ClassesType? = null,
    rawType: String? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberChip(number = number)
        if (type != null) TypeLabel(type = type, rawType = rawType)
    }
}

@Composable
private fun NumberChip(number: Int) {
    val classesNumbers = stringArrayResource(StringArrays.classes_numbers)
    Text(
        text = classesNumbers.getOrElse(number) { classesNumbers[0] }.uppercase(),
        style = MpeixTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MpeixTheme.palette.onSurface,
        maxLines = 1,
        modifier = Modifier
            .background(MpeixTheme.palette.outlineVariant, ChipShape)
            .padding(horizontal = 8.dp, vertical = 1.dp),
    )
}

/**
 * Colored dot and the name of the classes type.
 */
@Composable
private fun TypeLabel(
    type: ClassesType,
    rawType: String?,
) {
    val classesTypes = stringArrayResource(StringArrays.classes_types)
    Row(
        horizontalArrangement = Arrangement.spacedBy(MarkerSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(MarkerSize)
                .background(type.tagColor(), CircleShape),
        )
        Text(
            text = classesTypeName(type, rawType, classesTypes).uppercase(),
            style = MpeixTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MpeixTheme.palette.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ClassesName(name: String) {
    Text(
        text = name,
        style = MpeixTheme.typography.bodyLarge,
        fontSize = 18.sp,
        color = MpeixTheme.palette.onSurface,
    )
}

/**
 * Place, teacher and group lines, every one of them is optional.
 */
@Composable
private fun MetaInfo(
    place: String?,
    teacher: String?,
    group: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MetaLineSpacing)) {
        if (place != null) PlaceText(place = place)
        if (teacher != null) SecondaryText(text = teacher)
        if (group != null) SecondaryText(text = group)
    }
}

@Composable
private fun PlaceText(place: String) {
    Text(
        text = place,
        style = MpeixTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MpeixTheme.palette.onSurfaceVariant,
    )
}

@Composable
private fun SecondaryText(text: String) {
    Text(
        text = text,
        style = MpeixTheme.typography.bodyMedium,
        color = MpeixTheme.palette.onSurfaceVariant,
    )
}

/**
 * Container of the blocks of the parallel classes, see [SubgroupBlock] and [TypedBlock].
 */
@Composable
private fun ParallelBlocks(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

/**
 * Block of one of the subgroups with the same type, marked with the "split" icon.
 */
@Composable
private fun SubgroupBlock(content: @Composable () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(MarkerSpacing)) {
        Icon(
            imageVector = ArrowSplit,
            contentDescription = null,
            tint = MpeixTheme.palette.onSurfaceVariant,
            modifier = Modifier
                // Centers the icon on the first bodyMedium line (19sp line height)
                .padding(top = 1.5.dp)
                .size(MarkerSize),
        )
        content()
    }
}

/**
 * Block of one of the classes with its own type: the [TypeLabel] goes first,
 * the [content] is aligned with the type name.
 */
@Composable
private fun TypedBlock(
    type: ClassesType,
    rawType: String?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MetaLineSpacing)) {
        TypeLabel(type = type, rawType = rawType)
        Column(
            modifier = Modifier.padding(start = MarkerSize + MarkerSpacing),
            verticalArrangement = Arrangement.spacedBy(MetaLineSpacing),
            content = content,
        )
    }
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

/**
 * Color of the classes type circle. Same colors as in the old RecyclerView item,
 * they don't depend on the theme.
 */
@Composable
private fun ClassesType.tagColor(): Color =
    when (this) {
        ClassesType.LECTURE -> LectureTagColor
        ClassesType.PRACTICE -> PracticeTagColor
        ClassesType.LAB -> LabTagColor
        else -> MpeixTheme.palette.outlineVariant
    }

/**
 * Place shared by all the parallel classes, or null if they are in different places.
 */
private inline fun <T> List<T>.commonPlace(place: (T) -> String): String? =
    map(place).distinct().singleOrNull()

// region Previews

private val PreviewTime = ClassesTimeUi(startTime = LocalTime.of(9, 20), endTime = LocalTime.of(10, 55))

private val PreviewSingle = ParallelClassesUi.Single(
    name = "Инженерная и компьютерная графика",
    type = ClassesType.LECTURE,
    rawType = null,
    place = "Аудитория Б-407",
    group = null,
    teacher = "Чахеев Е.Я.",
    number = 1,
    time = PreviewTime,
    notePreview = null,
    progress = null,
)

private val PreviewSameNameAndType = ParallelClassesUi.SameNameAndType(
    name = "Электротехническое материаловедение",
    type = ClassesType.LAB,
    rawType = null,
    differentMetaInfo = listOf(
        ClassesMinimalMetaUi(place = "Е-306", group = "А-08-19\\1п", teacher = "Чахеев Е.Я."),
        ClassesMinimalMetaUi(place = "Е-308", group = "А-08-19\\2п", teacher = "Иванов И.И."),
    ),
    number = 3,
    time = PreviewTime,
    notePreview = null,
    progress = null,
)

private val PreviewSameNameDifferentTypes = ParallelClassesUi.SameNameDifferentTypes(
    name = "Теоретическая механика",
    differentMetaInfo = listOf(
        ClassesExtendedMetaUi(
            type = ClassesType.LECTURE,
            rawType = null,
            place = "Б-407",
            group = "А-08-19\\1п",
            teacher = "Чахеев Е.Я.",
        ),
        ClassesExtendedMetaUi(
            type = ClassesType.PRACTICE,
            rawType = null,
            place = "Б-408",
            group = "А-08-19\\2п",
            teacher = "Иванов И.И.",
        ),
    ),
    number = 2,
    time = PreviewTime,
    notePreview = null,
    progress = null,
)

@Composable
private fun ClassesItemPreviewColumn(content: @Composable ColumnScope.() -> Unit) {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}

@PreviewLightDark
@Composable
private fun SingleClassesItemPreview() {
    ClassesItemPreviewColumn {
        ClassesItem(model = PreviewSingle, onClick = {})
        ClassesItem(
            model = PreviewSingle.copy(
                name = "Технология металлов",
                type = ClassesType.PRACTICE,
                number = 2,
                group = "А-08-19\\1п",
                notePreview = "Принести типовой расчет №1 и все-все остальные расчеты",
            ),
        )
        ClassesItem(
            model = PreviewSingle.copy(
                type = ClassesType.UNDEFINED,
                rawType = "Зачёт",
                number = 4,
                teacher = null,
                group = "А-08-19, А-12-19",
                notePreview = "Не забыть зачётку",
                progress = 0.8f,
            ),
        )
    }
}

@PreviewLightDark
@Composable
private fun SameNameAndTypeClassesItemPreview() {
    ClassesItemPreviewColumn {
        ClassesItem(model = PreviewSameNameAndType.copy(progress = 0.5f), onClick = {})
        ClassesItem(
            model = PreviewSameNameAndType.copy(
                differentMetaInfo = PreviewSameNameAndType.differentMetaInfo.map { it.copy(place = "Б-407") },
                notePreview = "Не забыть халат",
            ),
        )
    }
}

@PreviewLightDark
@Composable
private fun SameNameDifferentTypesClassesItemPreview() {
    ClassesItemPreviewColumn {
        ClassesItem(model = PreviewSameNameDifferentTypes, onClick = {})
        ClassesItem(
            model = PreviewSameNameDifferentTypes.copy(
                differentMetaInfo = PreviewSameNameDifferentTypes.differentMetaInfo.map { it.copy(place = "Б-407") },
                notePreview = "Повторить вторую главу",
                progress = 0.3f,
            ),
        )
    }
}

// endregion
