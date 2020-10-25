package kekmech.ru.feature_dashboard

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.feature_dashboard.presentation.DashboardState
import java.time.DayOfWeek

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