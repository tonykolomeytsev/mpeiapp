package kekmech.ru.library_schedule.utils

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.atStartOfWeek(): LocalDate = let {
    if (dayOfWeek == DayOfWeek.MONDAY) it else minusDays(dayOfWeek.value - 1L)
}
