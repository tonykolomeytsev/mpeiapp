package kekmech.ru.feature_dashboard.helpers

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.domain_schedule.dto.Time
import kekmech.ru.feature_dashboard.elm.NextClassesCondition.*
import kekmech.ru.feature_dashboard.elm.NextClassesTimeStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val MINUTES_IN_HOUR = 60L

fun getNextClassesTimeStatus(nextClassesDate: LocalDate, nextClassesTime: Time): NextClassesTimeStatus {
    val currentMoscowDateTime = LocalDateTime.of(moscowLocalDate(), moscowLocalTime())
    val nextClassesStartDateTime = LocalDateTime.of(nextClassesDate, nextClassesTime.start)
    val nextClassesEndDateTime = LocalDateTime.of(nextClassesDate, nextClassesTime.end)

    return when {
        currentMoscowDateTime < nextClassesStartDateTime -> {
            // пара еще не началась
            val hoursUntilClasses = currentMoscowDateTime
                .until(nextClassesStartDateTime, ChronoUnit.HOURS)
            val minutesUntilClasses = currentMoscowDateTime
                .until(nextClassesStartDateTime, ChronoUnit.MINUTES)
            val minutesWithoutHours = minutesUntilClasses - (hoursUntilClasses * MINUTES_IN_HOUR)
            NextClassesTimeStatus(NOT_STARTED, hoursUntilClasses, minutesWithoutHours)
        }
        currentMoscowDateTime in nextClassesStartDateTime..nextClassesEndDateTime -> {
            // пара уже идет
            NextClassesTimeStatus(STARTED)
        }
        else -> NextClassesTimeStatus(ENDED)
    }
}
