package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.Context
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver

/** Makes sure the class reminders are planned on every app start. */
internal class ClassReminderLifecycleObserver(
    private val controller: ClassReminderController,
) : AppLifecycleObserver {

    override fun onCreate(context: Context) {
        controller.ensureScheduled()
    }
}
