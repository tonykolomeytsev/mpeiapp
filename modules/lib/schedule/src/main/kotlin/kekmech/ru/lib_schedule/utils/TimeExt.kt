package kekmech.ru.lib_schedule.utils

import java.time.DayOfWeek
import java.time.LocalDate

public fun LocalDate.atStartOfWeek(): LocalDate = let {
    if (dayOfWeek == DayOfWeek.MONDAY) it else minusDays(dayOfWeek.value - 1L)
}
