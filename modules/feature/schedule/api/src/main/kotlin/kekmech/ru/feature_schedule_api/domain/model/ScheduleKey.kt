package kekmech.ru.feature_schedule_api.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

/**
 * One week of one schedule.
 *
 * The week is identified by its Monday in Moscow time, not by an offset from today:
 * an offset points to another week after midnight on Monday, a date doesn't.
 * Use [of] to build a key from an offset.
 */
public data class ScheduleKey(
    val type: ScheduleType,
    val name: String,
    val firstDayOfWeek: LocalDate,
) {

    public companion object {

        private val MoscowZone: ZoneId = ZoneId.of("Europe/Moscow")

        /**
         * @param weekOffset 0 is the current week, 1 is the next one, -1 is the previous one
         */
        public fun of(
            type: ScheduleType,
            name: String,
            weekOffset: Int,
            today: LocalDate = LocalDate.now(MoscowZone),
        ): ScheduleKey =
            ScheduleKey(
                type = type,
                name = name,
                firstDayOfWeek = today.with(DayOfWeek.MONDAY).plusWeeks(weekOffset.toLong()),
            )

        public fun of(
            selectedSchedule: SelectedSchedule,
            weekOffset: Int,
            today: LocalDate = LocalDate.now(MoscowZone),
        ): ScheduleKey =
            of(
                type = selectedSchedule.type,
                name = selectedSchedule.name,
                weekOffset = weekOffset,
                today = today,
            )
    }
}
