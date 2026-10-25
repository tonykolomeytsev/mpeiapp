package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenScheduleController
import org.koin.core.context.GlobalContext

/**
 * Alarms are lost after a reboot, an app update and are wrong after a time zone / clock change:
 * re-plan them.
 */
internal class ClassReminderSystemReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED -> GlobalContext.getOrNull()?.let { koin ->
                koin.get<ClassReminderController>().ensureScheduled()
                koin.get<LockScreenScheduleController>().ensureScheduled()
            }
        }
    }
}
