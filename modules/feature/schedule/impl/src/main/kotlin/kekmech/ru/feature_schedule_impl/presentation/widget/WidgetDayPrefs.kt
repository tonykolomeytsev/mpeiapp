package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.Context
import androidx.core.content.edit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

/**
 * The day every widget instance shows, as the number of days from today.
 * It is remembered together with the date it was chosen on, so the next morning the widget is on "today" again.
 */
internal object WidgetDayPrefs {

    private const val PREFS_NAME = "schedule_widget_day"
    private const val MAX_SHIFT_DAYS = 28

    private fun key(appWidgetId: Int, name: String) = "${name}_$appWidgetId"

    fun offset(context: Context, appWidgetId: Int, today: LocalDate = LocalDate.now()): Int {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return resolveOffset(
            storedOffset = prefs.getInt(key(appWidgetId, "offset"), 0),
            storedAnchorEpochDay = prefs.getLong(key(appWidgetId, "anchor"), 0L),
            today = today,
        )
    }

    /** [delta] days forward (positive) or back (negative) */
    fun shift(context: Context, appWidgetId: Int, delta: Int, today: LocalDate = LocalDate.now()) {
        set(context, appWidgetId, shiftOffset(offset(context, appWidgetId, today), delta), today)
    }

    fun reset(context: Context, appWidgetId: Int, today: LocalDate = LocalDate.now()) {
        set(context, appWidgetId, 0, today)
    }

    private fun set(context: Context, appWidgetId: Int, offset: Int, today: LocalDate) {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putInt(key(appWidgetId, "offset"), offset)
            putLong(key(appWidgetId, "anchor"), today.toEpochDay())
        }
    }

    fun clear(context: Context, appWidgetIds: IntArray) {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            appWidgetIds.forEach {
                remove(key(it, "offset"))
                remove(key(it, "anchor"))
            }
        }
    }

    /** An offset chosen on another day is outdated: "today" moved on */
    internal fun resolveOffset(storedOffset: Int, storedAnchorEpochDay: Long, today: LocalDate): Int =
        if (storedAnchorEpochDay == today.toEpochDay()) storedOffset else 0

    internal fun shiftOffset(current: Int, delta: Int): Int =
        (current + delta).coerceIn(-MAX_SHIFT_DAYS, MAX_SHIFT_DAYS)

    /** How many weeks the week of [date] is away from the week of [today] (what the schedule repository takes) */
    internal fun weekOffset(today: LocalDate, date: LocalDate): Int {
        val monday = TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        return ChronoUnit.WEEKS.between(today.with(monday), date.with(monday)).toInt()
    }
}
