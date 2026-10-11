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
