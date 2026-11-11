package kekmech.ru.feature_schedule_ui.items

import java.time.LocalTime

/**
 * Marker of the gap between classes, inserted into the day list by `withWindows()`
 */
public data class WindowItem(
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)
