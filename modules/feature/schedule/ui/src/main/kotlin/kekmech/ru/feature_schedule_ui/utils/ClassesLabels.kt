package kekmech.ru.feature_schedule_ui.utils

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType

private val SUBGROUP_MARKER_PATTERN = """\\\d([пП])""".toRegex()

/**
 * Teacher of the classes for group schedules. Null for person schedules (the teacher is the
 * owner of the schedule), or if there is nothing meaningful to show.
 *
 * @param scheduleType type of the schedule the classes belong to
 */
internal fun Classes.teacherLabel(scheduleType: ScheduleType): String? =
    when (scheduleType) {
        ScheduleType.GROUP -> person.takeIfNotEmpty()
        ScheduleType.PERSON -> null
    }

/**
 * Groups of the classes: all the groups for person schedules, and only the subgroup for group
 * schedules (the group is the owner of the schedule). Null if there is nothing meaningful to show.
 *
 * @param scheduleType type of the schedule the classes belong to
 */
internal fun Classes.groupLabel(scheduleType: ScheduleType): String? =
    when (scheduleType) {
        // Покажем номер подгруппы, т.к. в большинстве случаев его наличие говорит о
        // "сдвоенных" парах в расписании, расположенных в одно время. Обычно относится к
        // разделению на подгруппы и т.д.
        ScheduleType.GROUP -> groups.takeIfNotEmpty()?.takeIf(SUBGROUP_MARKER_PATTERN::containsMatchIn)
        ScheduleType.PERSON -> groups.takeIfNotEmpty()
    }

/**
 * Index of the classes type name in the `classes_types` string array.
 */
@Suppress("MagicNumber")
internal val ClassesType.nameIndex: Int
    get() = when (this) {
        ClassesType.UNDEFINED -> 0
        ClassesType.LECTURE -> 1
        ClassesType.PRACTICE -> 2
        ClassesType.LAB -> 3
        ClassesType.COURSE -> 4
        ClassesType.CONSULTATION -> 5
        ClassesType.EXAM -> 6
    }

/**
 * Type name of the classes, falls back to the [rawType] from the backend for unknown types.
 *
 * @param classesTypes contents of the `classes_types` string array
 */
internal fun classesTypeName(
    type: ClassesType,
    rawType: String?,
    classesTypes: Array<String>,
): String =
    if (type == ClassesType.UNDEFINED && rawType != null) {
        rawType
    } else {
        classesTypes.getOrNull(type.nameIndex).orEmpty()
    }

private fun String.takeIfNotEmpty() = takeIf {
    isNotBlank() && !it.contains("Вакансия", ignoreCase = true)
}
