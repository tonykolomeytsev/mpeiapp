package kekmech.ru.feature_schedule.main.utils

import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.item.WeekItem
import java.time.LocalDate

object TimeUtils {

    fun createWeekItem(weekOffset: Int, firstDayOfWeek: LocalDate) =
        WeekItem(weekOffset, firstDayOfWeek, getDayItemsFor(weekOffset, firstDayOfWeek))

    fun getDayItemsFor(weekOffset: Int, selectedWeek: LocalDate): List<DayItem> {
        return List(6) { selectedWeek.plusDays(it.toLong()) }.map { DayItem(it, weekOffset, false) }
    }
}