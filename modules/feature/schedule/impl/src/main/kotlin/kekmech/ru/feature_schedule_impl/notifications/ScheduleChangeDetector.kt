package kekmech.ru.feature_schedule_impl.notifications

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.Schedule
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
    fun getDescription(): String = when (type) {
        ChangeType.ADDED -> "Добавлена пара: ${newClass?.name}"
        ChangeType.REMOVED -> "Отменена пара: ${oldClass?.name}"
        ChangeType.MODIFIED -> buildModificationDescription()
        ChangeType.REPLACED -> "Заменена пара: ${oldClass?.name} → ${newClass?.name}"
    }
    
    private fun buildModificationDescription(): String {
        val changes = mutableListOf<String>()
        
        if (oldClass?.time != newClass?.time) {
            changes.add("время (${oldClass?.time?.start} → ${newClass?.time?.start})")
        }
        if (oldClass?.place != newClass?.place) {
            changes.add("аудитория (${oldClass?.place} → ${newClass?.place})")
        }
        if (oldClass?.person != newClass?.person) {
            changes.add("преподаватель")
        }
        if (oldClass?.type != newClass?.type) {
            changes.add("тип")
        }
        
        return "Изменена пара ${newClass?.name}: ${changes.joinToString(", ")}"
    }
}

/**
 * Compares two schedules and detects changes
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
        newClasses: List<Classes>
    ): List<ScheduleChange> {
        val changes = mutableListOf<ScheduleChange>()
        
        // Find removed classes
        oldClasses.forEach { oldClass ->
            val matchingNewClass = newClasses.find { it.number == oldClass.number }
            
            when {
                matchingNewClass == null -> {
                    // Class removed
                    changes.add(
                        ScheduleChange(
                            type = ScheduleChange.ChangeType.REMOVED,
                            date = date,
                            oldClass = oldClass,
                            newClass = null
                        )
                    )
                }
                !areClassesEqual(oldClass, matchingNewClass) -> {
                    // Class modified
                    if (oldClass.name != matchingNewClass.name) {
                        // Completely different class - replaced
                        changes.add(
                            ScheduleChange(
                                type = ScheduleChange.ChangeType.REPLACED,
                                date = date,
                                oldClass = oldClass,
                                newClass = matchingNewClass
                            )
                        )
                    } else {
                        // Same class but details changed
                        changes.add(
                            ScheduleChange(
                                type = ScheduleChange.ChangeType.MODIFIED,
                                date = date,
                                oldClass = oldClass,
                                newClass = matchingNewClass
                            )
                        )
                    }
                }
            }
        }
        
        // Find added classes
        newClasses.forEach { newClass ->
            val matchingOldClass = oldClasses.find { it.number == newClass.number }
            
            if (matchingOldClass == null) {
                // New class added
                changes.add(
                    ScheduleChange(
                        type = ScheduleChange.ChangeType.ADDED,
                        date = date,
                        oldClass = null,
                        newClass = newClass
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
