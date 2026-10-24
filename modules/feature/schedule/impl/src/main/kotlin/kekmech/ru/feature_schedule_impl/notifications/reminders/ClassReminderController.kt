package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.activeKinds
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.areRemindersEnabled
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.setRemindersEnabled
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.setKindEnabled
import java.util.concurrent.TimeUnit

/**
 * Entry point for everything that changes the class reminders:
 * settings changes and sync triggers (app start, schedule change, reboot).
 */
internal class ClassReminderController(
    private val context: Context,
    private val alarmScheduler: ClassReminderAlarmScheduler,
) {

    private val workManager get() = WorkManager.getInstance(context)
    private val prefs get() = ClassReminderPrefs.of(context)

    fun setEnabled(enabled: Boolean) {
        prefs.setRemindersEnabled(enabled)
        applySettings()
    }

    fun setKindEnabled(kind: ReminderKind, enabled: Boolean) {
        prefs.setKindEnabled(kind, enabled)
        applySettings()
    }

    /** Called on app start: keeps the periodic work alive and refreshes the alarms */
    fun ensureScheduled() {
        if (prefs.areRemindersEnabled()) {
            enqueuePeriodic()
            syncNow()
        }
    }

    /** Re-plan the alarms soon, e.g. the selected schedule or the time zone changed */
    fun syncNow() {
        if (prefs.activeKinds().isEmpty()) return
        workManager.enqueueUniqueWork(
            ClassReminderSyncWorker.IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ClassReminderSyncWorker>().build(),
        )
    }

    private fun applySettings() {
        if (prefs.activeKinds().isEmpty()) {
            workManager.cancelUniqueWork(ClassReminderSyncWorker.PERIODIC_WORK_NAME)
            workManager.cancelUniqueWork(ClassReminderSyncWorker.IMMEDIATE_WORK_NAME)
            alarmScheduler.cancelAll()
        } else {
            enqueuePeriodic()
            // the alarms of a switched-off kind must disappear and new ones appear immediately
            workManager.enqueueUniqueWork(
                ClassReminderSyncWorker.IMMEDIATE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<ClassReminderSyncWorker>().build(),
            )
        }
    }

    private fun enqueuePeriodic() {
        val request = PeriodicWorkRequestBuilder<ClassReminderSyncWorker>(
            repeatInterval = SYNC_INTERVAL_HOURS,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        )
            .setConstraints(Constraints.NONE)
            .build()
        workManager.enqueueUniquePeriodicWork(
            ClassReminderSyncWorker.PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    private companion object {
        /** Alarms are registered for 3 weeks ahead, twice a day is plenty to follow schedule changes */
        const val SYNC_INTERVAL_HOURS = 12L
    }
}
