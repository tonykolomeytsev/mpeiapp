package kekmech.ru.feature_dashboard

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.Time
import kekmech.ru.feature_dashboard.presentation.DashboardState
import kekmech.ru.feature_dashboard.presentation.NextClassesCondition.*
import kekmech.ru.feature_dashboard.presentation.NextClassesTimeStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun DashboardState.getActualScheduleDayForView(): Day? {
    val nowDate = moscowLocalDate()
    val nowTime = moscowLocalTime()

    val isSunday = nowDate.dayOfWeek == DayOfWeek.SUNDAY
    val isEvening = todayClasses?.lastOrNull()?.time?.end?.let { it < nowTime } ?: false
    val hasNoClassesToday = todayClasses.isNullOrEmpty()
    val realToday = listOfNotNull(tomorrow, today).find { it.date == nowDate }

    return when {
        isSunday || isEvening || hasNoClassesToday -> tomorrow
        else -> realToday
    }
}

fun DashboardState.getNextClassesTimeStatus(nextClassesDate: LocalDate, nextClassesTime: Time): NextClassesTimeStatus {
    val currentMoscowDateTime = LocalDateTime.of(moscowLocalDate(), moscowLocalTime())
    val nextClassesStartDateTime = LocalDateTime.of(nextClassesDate, nextClassesTime.start)
    val nextClassesEndDateTime = LocalDateTime.of(nextClassesDate, nextClassesTime.end)

    return when {
        currentMoscowDateTime < nextClassesStartDateTime -> {
            // пара еще не началась
            val hoursUntilClasses = currentMoscowDateTime.until(nextClassesStartDateTime, ChronoUnit.HOURS)
            val minutesUntilClasses = currentMoscowDateTime.until(nextClassesStartDateTime, ChronoUnit.MINUTES)
            val minutesWithoutHours = minutesUntilClasses - (hoursUntilClasses * 60)
            NextClassesTimeStatus(NOT_STARTED, hoursUntilClasses, minutesWithoutHours)
        }
        currentMoscowDateTime in nextClassesStartDateTime..nextClassesEndDateTime -> {
            // пара уже идет
            NextClassesTimeStatus(STARTED)
        }
        else -> NextClassesTimeStatus(ENDED)
    }
}