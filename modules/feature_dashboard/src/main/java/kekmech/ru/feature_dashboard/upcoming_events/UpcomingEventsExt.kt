package kekmech.ru.feature_dashboard.upcoming_events

import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.feature_dashboard.elm.DashboardState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

private const val MAX_DAY_OFFSET = 7
private const val NONE_OFFSET = -1

/**
 * Получение списка пар на день, имеющий номер **offset** от сегодняшнего.
 * Для сегодняшнего дня **offset == 0**.
 *
 * @param currentDay текущая дата
 * @param offset отступ от текущего дня на **offset** дней вперед
 */
fun DashboardState.getDayWithOffset(
    currentDay: LocalDate,
    offset: Int,
): Day? {
    val dayIsOnCurrentWeek = currentDay.dayOfWeek.value + offset <= DayOfWeek.SUNDAY.value
    val dayWithOffset = currentDay.plusDays(offset.toLong())
    return (if (dayIsOnCurrentWeek) currentWeekSchedule else nextWeekSchedule)
        ?.weeks
        ?.firstOrNull()
        ?.days
        ?.find { it.date == dayWithOffset }
}

/**
 * Получение списка пар на день, имеющий номер **offset** от сегодняшнего.
 * Для сегодняшнего дня **offset == 0**.
 *
 * @param currentDay текущая дата
 * @param currentTime текущее время
 * @param offset отступ от текущего дня на **offset** дней вперед
 */
fun DashboardState.getClassesForDayWithOffset(
    currentDay: LocalDate,
    currentTime: LocalTime,
    offset: Int,
): List<Classes>? {
    val dayIsOnCurrentWeek = currentDay.dayOfWeek.value + offset <= DayOfWeek.SUNDAY.value
    val dayWithOffset = currentDay.plusDays(offset.toLong())
    return (if (dayIsOnCurrentWeek) currentWeekSchedule else nextWeekSchedule)
        ?.weeks
        ?.firstOrNull()
        ?.days
        ?.find { it.date == dayWithOffset }
        ?.classes
        ?.filterOnlyActual(offset, currentTime)
}

/**
 * Проверка наличия пар на день, имеющий номер **offset** от сегодняшнего.
 * Для сегодняшнего дня **offset == 0**.
 *
 * @param currentDay текущая дата
 * @param currentTime текущее время
 * @param offset отступ от текущего дня на **offset** дней вперед
 */
fun DashboardState.hasClassesForDayWithOffset(
    currentDay: LocalDate,
    currentTime: LocalTime,
    offset: Int,
): Boolean {
    val dayIsOnCurrentWeek = currentDay.dayOfWeek.value + offset <= DayOfWeek.SUNDAY.value
    val dayWithOffset = currentDay.plusDays(offset.toLong())
    return (if (dayIsOnCurrentWeek) currentWeekSchedule else nextWeekSchedule)
        ?.weeks
        ?.firstOrNull()
        ?.days
        ?.any { it.date == dayWithOffset && it.classes.hasActualClasses(offset, currentTime) }
        ?: false
}

/**
 * Получение **offset** дня, который требуется показать.
 * Если возвращает 0 - следует показать сегодняшние пары.
 * Если возвращает 1 - следует показать завтрашние пары и т.д.
 * Если возвращает -1 - подходящих дней для отображения не найдено.
 *
 * @param currentDay текущая дата
 * @param currentTime текущее время
 */
fun DashboardState.getOffsetForDayWithActualEvents(
    currentDay: LocalDate,
    currentTime: LocalTime,
): Int {
    var offset = 0
    var foundActualDay = hasClassesForDayWithOffset(currentDay, currentTime, offset)
    while (offset < MAX_DAY_OFFSET && !foundActualDay) {
        foundActualDay = hasClassesForDayWithOffset(currentDay, currentTime, ++offset)
    }
    return if (offset == MAX_DAY_OFFSET) NONE_OFFSET else offset
}

private fun List<Classes>.hasActualClasses(offset: Int, currentTime: LocalTime): Boolean =
    if (offset == 0) any { it.time.end > currentTime } else isNotEmpty()

private fun List<Classes>.filterOnlyActual(offset: Int, currentTime: LocalTime) =
    if (offset == 0) filter { it.time.end > currentTime } else this
