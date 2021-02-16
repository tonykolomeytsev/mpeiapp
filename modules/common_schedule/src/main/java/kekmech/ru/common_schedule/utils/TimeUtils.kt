package kekmech.ru.common_schedule.utils

import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.items.WeekItem
import java.time.LocalDate

object TimeUtils {

    fun createWeekItem(weekOffset: Int, firstDayOfWeek: LocalDate) =
        WeekItem(weekOffset, firstDayOfWeek, getDayItemsFor(weekOffset, firstDayOfWeek))

    private fun getDayItemsFor(weekOffset: Int, selectedWeek: LocalDate): List<DayItem> {
        return List(6) { selectedWeek.plusDays(it.toLong()) }
            .map { DayItem(it, weekOffset, false) }
    }
}