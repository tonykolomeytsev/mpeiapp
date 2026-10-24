package kekmech.ru.feature_schedule_impl.notifications.reminders

import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * When to remind about a class.
 * [code] is a part of the stable alarm id, do not change it.
 */
internal enum class ReminderKind(val hoursBefore: Long, val code: Int) {
    DAY_BEFORE(hoursBefore = 24, code = 1),
    HOURS_BEFORE(hoursBefore = 5, code = 2),
}

internal data class ClassReminder(
    /** Stable between syncs: lets the scheduler update/cancel an already registered alarm */
    val id: Int,
    val kind: ReminderKind,
    val triggerAtMillis: Long,
    val startMillis: Long,
    val endMillis: Long,
    val name: String,
    val type: ClassesType,
    val place: String,
)

/**
 * Builds the list of reminders for practices and laboratory works.
 *
 * "Smart" parts:
 * - only practices and laboratory works are reminded, lectures are skipped;
 * - reminders that are already in the past are skipped;
 * - a reminder that would fire at night (23:00–07:00) is moved to 07:00,
 *   unless that leaves less than [MIN_LEAD] before the class.
 */
internal object ClassReminderPlanner {

    private val RemindedTypes = setOf(ClassesType.PRACTICE, ClassesType.LAB)
    private val QuietFrom: LocalTime = LocalTime.of(23, 0)
    private val QuietTo: LocalTime = LocalTime.of(7, 0)
    private val MIN_LEAD: Duration = Duration.ofMinutes(15)

    fun plan(
        schedules: List<Schedule>,
        now: Instant,
        zone: ZoneId,
        kinds: Set<ReminderKind>,
    ): List<ClassReminder> {
        if (kinds.isEmpty()) return emptyList()
        val reminders = mutableListOf<ClassReminder>()
        for (schedule in schedules) {
            for (week in schedule.weeks) {
                for (day in week.days) {
                    for (classes in day.classes) {
                        if (classes.type !in RemindedTypes) continue
                        val start = ZonedDateTime.of(day.date, classes.time.start, zone)
                        val end = ZonedDateTime.of(day.date, classes.time.end, zone)
                        for (kind in kinds) {
                            val trigger = triggerTime(start, kind, now) ?: continue
                            reminders += ClassReminder(
                                id = stableId(day.date, classes.time.start, classes.name, kind),
                                kind = kind,
                                triggerAtMillis = trigger.toInstant().toEpochMilli(),
                                startMillis = start.toInstant().toEpochMilli(),
                                endMillis = end.toInstant().toEpochMilli(),
                                name = classes.name,
                                type = classes.type,
                                place = classes.place,
                            )
                        }
                    }
                }
            }
        }
        // the same class can be present in several fetched weeks
        return reminders
            .distinctBy { it.id }
            .sortedBy { it.triggerAtMillis }
    }

    private fun triggerTime(start: ZonedDateTime, kind: ReminderKind, now: Instant): ZonedDateTime? {
        val trigger = shiftOutOfQuietHours(start.minusHours(kind.hoursBefore))
        return when {
            !trigger.toInstant().isAfter(now) -> null
            trigger.plus(MIN_LEAD).isAfter(start) -> null
            else -> trigger
        }
    }

    internal fun shiftOutOfQuietHours(time: ZonedDateTime): ZonedDateTime {
        val localTime = time.toLocalTime()
        return when {
            localTime >= QuietFrom -> time.toLocalDate().plusDays(1).atTime(QuietTo).atZone(time.zone)
            localTime < QuietTo -> time.toLocalDate().atTime(QuietTo).atZone(time.zone)
            else -> time
        }
    }

    private fun stableId(date: LocalDate, start: LocalTime, name: String, kind: ReminderKind): Int =
        "$date|$start|$name|${kind.code}".hashCode() and Int.MAX_VALUE
}
