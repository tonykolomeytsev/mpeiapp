package kekmech.ru.feature_schedule_ui.utils

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType

private val SUBGROUP_MARKER_PATTERN = """\\\d([пП])""".toRegex()

/**
 * Secondary label of the classes: the person and subgroup for group schedules,
 * or the groups for person schedules. Null if there is nothing meaningful to show.
 */
internal fun Classes.secondaryLabel(): String? =
    when (scheduleType) {
        ScheduleType.GROUP -> buildGroupLabel().takeIfNotEmpty()
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
 * Type name of the classes, falls back to the raw type from the backend for unknown types.
 */
internal fun Classes.typeName(classesTypes: Array<String>): String =
    if (type == ClassesType.UNDEFINED && rawType != null) {
        rawType.orEmpty()
    } else {
        classesTypes.getOrNull(type.nameIndex).orEmpty()
    }

private fun Classes.buildGroupLabel(): String =
    buildString {
        val hasSubgroupLabel = SUBGROUP_MARKER_PATTERN.containsMatchIn(groups)
        if (person.isNotEmpty()) {
            append(person)
            if (hasSubgroupLabel) append('\n')
        }
        // Покажем еще и номер подгруппы, т.к. в большинстве случаев его наличие говорит о
        // "сдвоенных" парах в расписании, расположенных в одно время. Обычно относится к
        // раздеделению на подгруппы и т.д.
        if (hasSubgroupLabel) {
            append(groups)
        }
    }

private fun String.takeIfNotEmpty() = takeIf {
    isNotBlank() && !it.contains("Вакансия", ignoreCase = true)
}
