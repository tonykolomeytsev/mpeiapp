package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.intervalHours
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.isEnabled
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver

/**
 * Makes sure periodic schedule checks are scheduled on app start
 * if the user has enabled change notifications.
 */
internal class ScheduleChangeLifecycleObserver(
    private val scheduler: ScheduleChangeScheduler,
) : AppLifecycleObserver {

    override fun onCreate(context: Context) {
        val prefs = ScheduleNotificationPrefs.of(context)
        if (prefs.isEnabled()) {
            scheduler.startPeriodicChecks(
                intervalHours = prefs.intervalHours(),
                replaceExisting = false,
            )
        }
    }
}
