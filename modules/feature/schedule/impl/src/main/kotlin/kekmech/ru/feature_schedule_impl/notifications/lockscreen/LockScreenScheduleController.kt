package kekmech.ru.feature_schedule_impl.notifications.lockscreen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenPrefs.isLockScreenEnabled
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenPrefs.setLockScreenEnabled
import java.util.concurrent.TimeUnit

/**
 * Keeps the lock screen notification up to date: the worker draws it, the alarm wakes the worker
 * at the moment the current / next class changes, the periodic work is the safety net.
 */
internal class LockScreenScheduleController(
    private val context: Context,
    private val notifier: LockScreenNotifier,
) {

    private val workManager get() = WorkManager.getInstance(context)
    private val alarmManager get() = context.getSystemService(AlarmManager::class.java)
    private val prefs get() = LockScreenPrefs.of(context)

    fun setEnabled(enabled: Boolean) {
        prefs.setLockScreenEnabled(enabled)
        if (enabled) {
            ensureScheduled()
        } else {
            workManager.cancelUniqueWork(LockScreenRefreshWorker.PERIODIC_WORK_NAME)
            workManager.cancelUniqueWork(LockScreenRefreshWorker.IMMEDIATE_WORK_NAME)
            cancelAlarm()
            notifier.hide()
        }
    }

    /** Called on app start, after a reboot, a time zone change, a schedule change */
    fun ensureScheduled() {
        if (!prefs.isLockScreenEnabled()) return
        val request = PeriodicWorkRequestBuilder<LockScreenRefreshWorker>(
            repeatInterval = REFRESH_INTERVAL_HOURS,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        )
            .setConstraints(Constraints.NONE)
            .build()
        workManager.enqueueUniquePeriodicWork(
            LockScreenRefreshWorker.PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
        refreshNow()
    }

    fun refreshNow() {
        if (!prefs.isLockScreenEnabled()) return
        workManager.enqueueUniqueWork(
            LockScreenRefreshWorker.IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<LockScreenRefreshWorker>().build(),
        )
    }

    /** Wakes the worker at [triggerAtMillis]; inexact alarms need no special permission */
    fun scheduleRefreshAt(triggerAtMillis: Long) {
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmIntent(PendingIntent.FLAG_UPDATE_CURRENT))
    }

    private fun cancelAlarm() {
        PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            Intent(context, LockScreenReceiver::class.java).setAction(LockScreenReceiver.ACTION),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        )?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    private fun alarmIntent(flag: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            Intent(context, LockScreenReceiver::class.java).setAction(LockScreenReceiver.ACTION),
            flag or PendingIntent.FLAG_IMMUTABLE,
        )

    private companion object {
        const val ALARM_REQUEST_CODE = 3003
        const val REFRESH_INTERVAL_HOURS = 6L
    }
}
