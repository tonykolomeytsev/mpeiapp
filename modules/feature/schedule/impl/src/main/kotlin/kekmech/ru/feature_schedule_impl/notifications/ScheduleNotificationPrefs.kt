package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Persistent settings and statistics of schedule change notifications.
 * Shared between the settings screen, the background worker and the app start observer.
 */
internal object ScheduleNotificationPrefs {

    const val PREFS_NAME = "schedule_notification_settings"
    const val KEY_ENABLED = "enabled"
    const val KEY_INTERVAL = "interval_hours"
    const val KEY_TOTAL_CHECKS = "total_checks"
    const val KEY_TOTAL_CHANGES = "total_changes"
    const val KEY_LAST_CHECK_MILLIS = "last_check_millis"

    fun of(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun SharedPreferences.isEnabled(): Boolean = getBoolean(KEY_ENABLED, false)

    fun SharedPreferences.intervalHours(): Long =
        getLong(KEY_INTERVAL, ScheduleChangeScheduler.DEFAULT_CHECK_INTERVAL_HOURS)

    /** Called by the worker after every finished check */
    @Synchronized
    fun recordCheck(context: Context, changesFound: Int) {
        val prefs = of(context)
        prefs.edit {
            putInt(KEY_TOTAL_CHECKS, prefs.getInt(KEY_TOTAL_CHECKS, 0) + 1)
            putInt(KEY_TOTAL_CHANGES, prefs.getInt(KEY_TOTAL_CHANGES, 0) + changesFound)
            putLong(KEY_LAST_CHECK_MILLIS, System.currentTimeMillis())
        }
    }
}
