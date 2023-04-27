package kekmech.ru.feature_dashboard.screens.main.helpers

import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.common_kotlin.moscowLocalTime
import kekmech.ru.domain_schedule_models.dto.Time
import kekmech.ru.feature_dashboard.screens.main.elm.NextClassesCondition.ENDED
import kekmech.ru.feature_dashboard.screens.main.elm.NextClassesCondition.NOT_STARTED
import kekmech.ru.feature_dashboard.screens.main.elm.NextClassesCondition.STARTED
import kekmech.ru.feature_dashboard.screens.main.elm.NextClassesTimeStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val MINUTES_IN_HOUR = 60L

internal fun getNextClassesTimeStatus(nextClassesDate: LocalDate, nextClassesTime: Time): NextClassesTimeStatus {
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
