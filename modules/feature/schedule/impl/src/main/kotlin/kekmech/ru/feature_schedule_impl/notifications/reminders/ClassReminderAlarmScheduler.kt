package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.saveScheduledIds
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.scheduledIds
import timber.log.Timber

/**
 * Registers class reminders in [AlarmManager].
 * Every reminder has its own [PendingIntent] (request code = reminder id), so registering the same
 * reminder again just updates it, and an outdated one can be cancelled by id.
 * Alarms are inexact (a few minutes late at most): the app does not request the exact alarm permission,
 * the reminders are sent hours before a class.
 */
internal class ClassReminderAlarmScheduler(
    private val context: Context,
) {

    private val alarmManager get() = context.getSystemService(AlarmManager::class.java)
    private val prefs get() = ClassReminderPrefs.of(context)

    /** Makes the registered alarms match [reminders] exactly */
    fun apply(reminders: List<ClassReminder>) {
        val previousIds = prefs.scheduledIds()
        val newIds = reminders.mapTo(mutableSetOf()) { it.id }
        (previousIds - newIds).forEach(::cancel)
        reminders.forEach(::schedule)
        prefs.saveScheduledIds(newIds)
        Timber.i("ClassReminderAlarmScheduler: ${reminders.size} reminders scheduled, ${(previousIds - newIds).size} cancelled")
    }

    fun cancelAll() {
        prefs.scheduledIds().forEach(::cancel)
        prefs.saveScheduledIds(emptySet())
    }

    private fun schedule(reminder: ClassReminder) {
        val pendingIntent = pendingIntent(reminder.id, reminder, PendingIntent.FLAG_UPDATE_CURRENT) ?: return
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.triggerAtMillis, pendingIntent)
    }

    private fun cancel(id: Int) {
        pendingIntent(id, reminder = null, flag = PendingIntent.FLAG_NO_CREATE)?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    private fun pendingIntent(id: Int, reminder: ClassReminder?, flag: Int): PendingIntent? {
        val intent = Intent(context, ClassReminderReceiver::class.java).setAction(ClassReminderReceiver.ACTION)
        reminder?.let {
            intent.putExtra(ClassReminderReceiver.EXTRA_ID, it.id)
                .putExtra(ClassReminderReceiver.EXTRA_KIND, it.kind.name)
                .putExtra(ClassReminderReceiver.EXTRA_START, it.startMillis)
                .putExtra(ClassReminderReceiver.EXTRA_END, it.endMillis)
                .putExtra(ClassReminderReceiver.EXTRA_NAME, it.name)
                .putExtra(ClassReminderReceiver.EXTRA_TYPE, it.type.name)
                .putExtra(ClassReminderReceiver.EXTRA_PLACE, it.place)
        }
        return PendingIntent.getBroadcast(context, id, intent, flag or PendingIntent.FLAG_IMMUTABLE)
    }
}
