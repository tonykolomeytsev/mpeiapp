package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.activeKinds
import timber.log.Timber

/** Fired by [android.app.AlarmManager] at the reminder time, shows the notification. */
internal class ClassReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION) return
        val kind = intent.getStringExtra(EXTRA_KIND)
            ?.let { name -> ReminderKind.entries.firstOrNull { it.name == name } }
            ?: return
        // the user could have switched the reminders off after the alarm was registered
        if (kind !in ClassReminderPrefs.of(context).activeKinds()) return

        val reminder = ClassReminder(
            id = intent.getIntExtra(EXTRA_ID, 0),
            kind = kind,
            triggerAtMillis = System.currentTimeMillis(),
            startMillis = intent.getLongExtra(EXTRA_START, 0L),
            endMillis = intent.getLongExtra(EXTRA_END, 0L),
            name = intent.getStringExtra(EXTRA_NAME).orEmpty(),
            type = intent.getStringExtra(EXTRA_TYPE)
                ?.let { name -> ClassesType.entries.firstOrNull { it.name == name } }
                ?: ClassesType.PRACTICE,
            place = intent.getStringExtra(EXTRA_PLACE).orEmpty(),
        )
        Timber.d("ClassReminderReceiver: ${reminder.kind} reminder for ${reminder.name}")
        ClassReminderNotifier(context).show(reminder)
    }

    companion object {
        const val ACTION = "kekmech.ru.feature_schedule_impl.CLASS_REMINDER"
        const val EXTRA_ID = "id"
        const val EXTRA_KIND = "kind"
        const val EXTRA_START = "start"
        const val EXTRA_END = "end"
        const val EXTRA_NAME = "name"
        const val EXTRA_TYPE = "type"
        const val EXTRA_PLACE = "place"
    }
}
