package kekmech.ru.feature_schedule_impl.notifications.lockscreen

import android.content.Context
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver

/** Makes sure the lock screen notification is alive on every app start. */
internal class LockScreenLifecycleObserver(
    private val controller: LockScreenScheduleController,
) : AppLifecycleObserver {

    override fun onCreate(context: Context) {
        controller.ensureScheduled()
    }
}
