package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.utils.groupLabel
import kekmech.ru.feature_schedule_ui.utils.teacherLabel
import java.time.LocalTime

/**
 * UI model of the [ClassesItem]: one or several "parallel" classes with the same name, which
 * take place at the same time and are shown as a single card. Contains only the data the card
 * renders, so that the domain [Classes] doesn't leak into the composables.
 *
 * Variants go from the simplest to the most complex one, every next variant has less
 * information in common between the parallel classes:
 * - [Single] — just one classes;
 * - [SameNameAndType] — e.g. labs of different subgroups in different places;
 * - [SameNameDifferentTypes] — e.g. lecture for one subgroup and practice for another.
 *
 * Use [toUiModel] to create it from the domain [Classes], and [groupParallelClasses]
 * to find the classes that should be shown as a single card.
 *
 * @property number number of the classes in the day, starting from 1
 * @property time start and end time of the classes
 * @property notePreview text of the note attached to the classes, or null if there is no note
 * @property progress progress of the ongoing classes from 0 to 1, or null if classes are not ongoing
 */
@Immutable
public sealed class ParallelClassesUi {

    public abstract val number: Int
    public abstract val time: ClassesTimeUi
    public abstract val notePreview: String?
    public abstract val progress: Float?

    /**
     * Just one classes, nothing parallel.
     */
    public data class Single(
        val name: String,
        val type: ClassesType,
        val rawType: String?,
        val place: String,
        val group: String?,
        val teacher: String?,
        override val number: Int,
        override val time: ClassesTimeUi,
        override val notePreview: String?,
        override val progress: Float?,
    ) : ParallelClassesUi()

    /**
     * Parallel classes with the same name and type, which differ only by place,
     * group and teacher. E.g. labs of different subgroups.
     */
    public data class SameNameAndType(
        val name: String,
        val type: ClassesType,
        val rawType: String?,
        val differentMetaInfo: List<ClassesMinimalMetaUi>,
        override val number: Int,
        override val time: ClassesTimeUi,
        override val notePreview: String?,
        override val progress: Float?,
    ) : ParallelClassesUi()

    /**
     * Parallel classes with the same name, but different types.
     * E.g. lecture for one subgroup and practice for another one.
     */
    public data class SameNameDifferentTypes(
        val name: String,
        val differentMetaInfo: List<ClassesExtendedMetaUi>,
        override val number: Int,
        override val time: ClassesTimeUi,
        override val notePreview: String?,
        override val progress: Float?,
    ) : ParallelClassesUi()
}

@Immutable
public data class ClassesTimeUi(
    val startTime: LocalTime,
    val endTime: LocalTime,
)

/**
 * One of the [ParallelClassesUi.SameNameDifferentTypes] classes, without the common name.
 */
@Immutable
public data class ClassesExtendedMetaUi(
    val type: ClassesType,
    val rawType: String?,
    val place: String,
    val group: String?,
    val teacher: String?,
)

/**
 * One of the [ParallelClassesUi.SameNameAndType] classes, without the common name and type.
 */
@Immutable
public data class ClassesMinimalMetaUi(
    val place: String,
    val group: String?,
    val teacher: String?,
)

/**
 * Maps the domain [Classes] to the [ParallelClassesUi.Single].
 *
 * @param scheduleType type of the schedule the classes belong to
 * @param notePreview text of the note attached to the classes, or null if there is no note
 * @param progress progress of the ongoing classes from 0 to 1, or null if classes are not ongoing
 */
public fun Classes.toUiModel(
    scheduleType: ScheduleType,
    // TODO: Pass the note explicitly, when the AttachNotesToScheduleService stops mutating Classes
    notePreview: String? = attachedNotePreview,
    progress: Float? = this.progress,
): ParallelClassesUi =
    listOf(this).toUiModel(scheduleType, notePreview, progress)

/**
 * Maps the parallel classes (see [groupParallelClasses]) to a single [ParallelClassesUi].
 * Picks the simplest variant which can describe all the classes, duplicates are dropped.
 * Name, number and time are taken from the first classes.
 *
 * All the classes must have the same name, see [groupParallelClasses] why.
 *
 * @param scheduleType type of the schedule the classes belong to
 * @param notePreview text of the note attached to the classes, or null if there is no note
 * @param progress progress of the ongoing classes from 0 to 1, or null if classes are not ongoing
 */
public fun List<Classes>.toUiModel(
    scheduleType: ScheduleType,
    notePreview: String? = firstNotNullOfOrNull(Classes::attachedNotePreview),
    progress: Float? = firstNotNullOfOrNull(Classes::progress),
): ParallelClassesUi {
    val first = requireNotNull(firstOrNull()) { "Can't create ParallelClassesUi from empty list" }
    require(all { it.name == first.name }) { "Parallel classes must have the same name: ${map(Classes::name)}" }
    val number = first.number
    val time = ClassesTimeUi(startTime = first.time.start, endTime = first.time.end)
    val metas = map { it.toExtendedMeta(scheduleType) }.distinct()
    val firstMeta = metas.first()
    return when {
        metas.size == 1 -> ParallelClassesUi.Single(
            name = first.name,
            type = firstMeta.type,
            rawType = firstMeta.rawType,
            place = firstMeta.place,
            group = firstMeta.group,
            teacher = firstMeta.teacher,
            number = number,
            time = time,
            notePreview = notePreview,
            progress = progress,
        )

        metas.all { it.hasSameType(firstMeta) } -> ParallelClassesUi.SameNameAndType(
            name = first.name,
            type = firstMeta.type,
            rawType = firstMeta.rawType,
            differentMetaInfo = metas.map { meta ->
                ClassesMinimalMetaUi(
                    place = meta.place,
                    group = meta.group,
                    teacher = meta.teacher,
                )
            },
            number = number,
            time = time,
            notePreview = notePreview,
            progress = progress,
        )

        else -> ParallelClassesUi.SameNameDifferentTypes(
            name = first.name,
            differentMetaInfo = metas,
            number = number,
            time = time,
            notePreview = notePreview,
            progress = progress,
        )
    }
}

/**
 * Groups the parallel classes which should be shown as a single [ClassesItem]:
 * classes with the same number, time and name. Keeps the original order of the classes.
 *
 * Classes with different names are never grouped, because notes are attached by the classes
 * name and start time: the card has a single click target, so it can't tell which of the
 * different classes the new note is for.
 */
public fun List<Classes>.groupParallelClasses(): List<List<Classes>> =
    groupBy { cls -> ParallelClassesKey(number = cls.number, time = cls.time, name = cls.name) }
        .values
        .toList()

private data class ParallelClassesKey(
    val number: Int,
    val time: Time,
    val name: String,
)

private fun Classes.toExtendedMeta(scheduleType: ScheduleType): ClassesExtendedMetaUi =
    ClassesExtendedMetaUi(
        type = type,
        rawType = rawType,
        place = place,
        group = groupLabel(scheduleType),
        teacher = teacherLabel(scheduleType),
    )

private fun ClassesExtendedMetaUi.hasSameType(other: ClassesExtendedMetaUi): Boolean =
    type == other.type && rawType == other.rawType
