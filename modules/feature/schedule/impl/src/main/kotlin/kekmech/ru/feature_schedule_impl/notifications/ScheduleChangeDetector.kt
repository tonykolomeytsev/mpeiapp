package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.res_strings.R.string as Strings
import java.time.LocalDate

/**
 * Data class representing a change in schedule
 */
internal data class ScheduleChange(
    val type: ChangeType,
    val date: LocalDate,
    val oldClass: Classes?,
    val newClass: Classes?
) {
    enum class ChangeType {
        ADDED,      // New class added
        REMOVED,    // Class removed
        MODIFIED,   // Class details changed (time, room, teacher, etc.)
        REPLACED    // Class completely replaced with another
    }
    
    /**
     * Get human-readable description of the change
     */
    fun getDescription(context: Context): String = when (type) {
        ChangeType.ADDED -> context.getString(Strings.schedule_change_added, newClass?.name.orEmpty())
        ChangeType.REMOVED -> context.getString(Strings.schedule_change_removed, oldClass?.name.orEmpty())
        ChangeType.MODIFIED -> context.getString(
            Strings.schedule_change_modified,
            newClass?.name.orEmpty(),
            buildModificationDescription(context),
        )
        ChangeType.REPLACED -> context.getString(
            Strings.schedule_change_replaced,
            oldClass?.name.orEmpty(),
            newClass?.name.orEmpty(),
        )
    }

    private fun buildModificationDescription(context: Context): String {
        val changes = mutableListOf<String>()

        if (oldClass?.time != newClass?.time) {
            changes.add(
                context.getString(
                    Strings.schedule_change_field_time,
                    oldClass?.time?.start.toString(),
                    newClass?.time?.start.toString(),
                )
            )
        }
        if (oldClass?.place != newClass?.place) {
            changes.add(
                context.getString(
                    Strings.schedule_change_field_place,
                    oldClass?.place.orEmpty(),
                    newClass?.place.orEmpty(),
                )
            )
        }
        if (oldClass?.person != newClass?.person) {
            changes.add(context.getString(Strings.schedule_change_field_person))
        }
        if (oldClass?.type != newClass?.type) {
            changes.add(context.getString(Strings.schedule_change_field_type))
        }

        return changes.joinToString(", ")
    }
}


/**
 * Многоуровневый детектор изменений расписания занятий.
 *
 * Алгоритм сопоставления:
 * 1. Группирует занятия по номерам слотов (1–7 пара).
 * 2. Шаг 1: Точные совпадения (предмет, преподаватель, аудитория, подгруппа, время) — удаляются без генерации событий.
 * 3. Шаг 2: Совпадение группы и названия (детали пары изменились) -> MODIFIED.
 * 4. Шаг 3: Совпадение подгруппы при смене дисциплины -> REPLACED (замена предмета).
 * 5. Шаг 4: Совпадение дисциплины при изменении группы -> MODIFIED.
 * 6. Шаг 5: Попарное сопоставление оставшихся занятий слота (один-к-одному).
 * 7. Шаг 6/7: Оставшиеся в старом расписании -> REMOVED, появившиеся в новом -> ADDED.
 */
internal object ScheduleChangeDetector {
    
    /**
     * Compare two schedules and return list of detected changes
     */
    fun detectChanges(
        oldSchedule: Schedule?,
        newSchedule: Schedule
    ): List<ScheduleChange> {
        if (oldSchedule == null) return emptyList()
        
        val changes = mutableListOf<ScheduleChange>()
        
        // Compare each week
        newSchedule.weeks.forEach { newWeek ->
            val oldWeek = oldSchedule.weeks.find { it.weekOfYear == newWeek.weekOfYear }
            
            if (oldWeek != null) {
                // Compare each day in the week
                newWeek.days.forEach { newDay ->
                    val oldDay = oldWeek.days.find { it.date == newDay.date }
                    
                    if (oldDay != null) {
                        // Detect changes in this day
                        changes.addAll(detectDayChanges(newDay.date, oldDay.classes, newDay.classes))
                    }
                }
            }
        }
        
        return changes
    }
    
    /**
     * Compare classes on a specific day
     */
    private fun detectDayChanges(
        date: LocalDate,
        oldClasses: List<Classes>,
        newClasses: List<Classes>,
    ): List<ScheduleChange> {
        val changes = mutableListOf<ScheduleChange>()

        val oldByNumber = oldClasses.groupBy { it.number }
        val newByNumber = newClasses.groupBy { it.number }
        val allNumbers = (oldByNumber.keys + newByNumber.keys).sorted()

        for (number in allNumbers) {
            val oldSlotClasses = oldByNumber[number].orEmpty().toMutableList()
            val newSlotClasses = newByNumber[number].orEmpty().toMutableList()

            // 1. Exact matches: identical classes in this slot (no change)
            val exactOldIterator = oldSlotClasses.iterator()
            while (exactOldIterator.hasNext()) {
                val oldClass = exactOldIterator.next()
                val matchIndex = newSlotClasses.indexOfFirst { areClassesEqual(oldClass, it) }
                if (matchIndex != -1) {
                    exactOldIterator.remove()
                    newSlotClasses.removeAt(matchIndex)
                }
            }

            // 2. Same groups and same name (subgroup and discipline matched, details changed)
            val groupAndNameOldIterator = oldSlotClasses.iterator()
            while (groupAndNameOldIterator.hasNext()) {
                val oldClass = groupAndNameOldIterator.next()
                val matchIndex = newSlotClasses.indexOfFirst {
                    it.name == oldClass.name && it.groups == oldClass.groups
                }
                if (matchIndex != -1) {
                    val matchingNewClass = newSlotClasses.removeAt(matchIndex)
                    groupAndNameOldIterator.remove()
                    changes.add(
                        ScheduleChange(
                            type = ScheduleChange.ChangeType.MODIFIED,
                            date = date,
                            oldClass = oldClass,
                            newClass = matchingNewClass,
                        )
                    )
                }
            }

            // 3. Same groups (same subgroup, but discipline changed: REPLACED)
            val groupOldIterator = oldSlotClasses.iterator()
            while (groupOldIterator.hasNext()) {
                val oldClass = groupOldIterator.next()
                if (oldClass.groups.isNotBlank()) {
                    val matchIndex = newSlotClasses.indexOfFirst {
                        it.groups.isNotBlank() && it.groups == oldClass.groups
                    }
                    if (matchIndex != -1) {
                        val matchingNewClass = newSlotClasses.removeAt(matchIndex)
                        groupOldIterator.remove()
                        val changeType = if (oldClass.name != matchingNewClass.name) {
                            ScheduleChange.ChangeType.REPLACED
                        } else {
                            ScheduleChange.ChangeType.MODIFIED
                        }
                        changes.add(
                            ScheduleChange(
                                type = changeType,
                                date = date,
                                oldClass = oldClass,
                                newClass = matchingNewClass,
                            )
                        )
                    }
                }
            }

            // 4. Same name (same discipline, but groups might differ or be empty)
            val nameOldIterator = oldSlotClasses.iterator()
            while (nameOldIterator.hasNext()) {
                val oldClass = nameOldIterator.next()
                val matchIndex = newSlotClasses.indexOfFirst { it.name == oldClass.name }
                if (matchIndex != -1) {
                    val matchingNewClass = newSlotClasses.removeAt(matchIndex)
                    nameOldIterator.remove()
                    changes.add(
                        ScheduleChange(
                            type = ScheduleChange.ChangeType.MODIFIED,
                            date = date,
                            oldClass = oldClass,
                            newClass = matchingNewClass,
                        )
                    )
                }
            }

            // 5. Pair remaining classes in this slot (one-to-one replacement)
            while (oldSlotClasses.isNotEmpty() && newSlotClasses.isNotEmpty()) {
                val oldClass = oldSlotClasses.removeAt(0)
                val matchingNewClass = newSlotClasses.removeAt(0)
                val changeType = if (oldClass.name != matchingNewClass.name) {
                    ScheduleChange.ChangeType.REPLACED
                } else {
                    ScheduleChange.ChangeType.MODIFIED
                }
                changes.add(
                    ScheduleChange(
                        type = changeType,
                        date = date,
                        oldClass = oldClass,
                        newClass = matchingNewClass,
                    )
                )
            }

            // 6. Leftover in old: REMOVED
            for (oldClass in oldSlotClasses) {
                changes.add(
                    ScheduleChange(
                        type = ScheduleChange.ChangeType.REMOVED,
                        date = date,
                        oldClass = oldClass,
                        newClass = null,
                    )
                )
            }

            // 7. Leftover in new: ADDED
            for (newClass in newSlotClasses) {
                changes.add(
                    ScheduleChange(
                        type = ScheduleChange.ChangeType.ADDED,
                        date = date,
                        oldClass = null,
                        newClass = newClass,
                    )
                )
            }
        }

        return changes
    }
    
    /**
     * Check if two classes are equal (ignoring UI fields)
     */
    private fun areClassesEqual(class1: Classes, class2: Classes): Boolean {
        return class1.name == class2.name &&
                class1.type == class2.type &&
                class1.place == class2.place &&
                class1.person == class2.person &&
                class1.time == class2.time &&
                class1.groups == class2.groups
    }
}
