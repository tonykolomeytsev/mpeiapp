package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kekmech.ru.ext_android.AppLocales
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kekmech.ru.res_icons.R.drawable as ResIcons
import kekmech.ru.res_strings.R.string as Strings

/** Shows the "practice / laboratory work is coming" notification. */
internal class ClassReminderNotifier(
    private val context: Context,
) {

    // the language chosen in the app, not the one of the system
    private val localized get() = AppLocales.localized(context)

    fun show(reminder: ClassReminder) {
        createChannel()
        val timeText = formatTimeRange(reminder)
        val details = listOf(timeText, reminder.place.takeIf { it.isNotBlank() })
            .filterNotNull()
            .joinToString(" • ")
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(ResIcons.ic_notification_schedule_24)
            .setContentTitle(title(reminder))
            .setContentText(reminder.name)
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminder.name + "\n" + details))
            .setSubText(details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setWhen(reminder.startMillis)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setContentIntent(openScheduleIntent())
            .build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_TAG, reminder.id, notification)
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS is not granted: the reminder is silently dropped
            Timber.w(e, "ClassReminderNotifier: permission denied")
        }
    }

    private fun title(reminder: ClassReminder): String {
        val type = localized.getString(
            if (reminder.type == ClassesType.LAB) Strings.class_reminder_type_lab else Strings.class_reminder_type_practice,
        )
        return localized.getString(
            when (reminder.kind) {
                ReminderKind.DAY_BEFORE -> Strings.class_reminder_title_day_before
                ReminderKind.HOURS_BEFORE -> Strings.class_reminder_title_hours_before
            },
            type,
        )
    }

    private fun formatTimeRange(reminder: ClassReminder): String {
        val zone = ZoneId.systemDefault()
        val start = Instant.ofEpochMilli(reminder.startMillis).atZone(zone).format(TIME_FORMATTER)
        val end = Instant.ofEpochMilli(reminder.endMillis).atZone(zone).format(TIME_FORMATTER)
        return "$start–$end"
    }

    /** Opens the schedule tab through the app deeplink */
    private fun openScheduleIntent(): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(SCHEDULE_DEEPLINK))
            .setPackage(context.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(
            context,
            SCHEDULE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                localized.getString(Strings.class_reminder_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = localized.getString(Strings.class_reminder_channel_description)
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private companion object {
        const val CHANNEL_ID = "class_reminders"
        const val NOTIFICATION_TAG = "class_reminder"
        const val SCHEDULE_DEEPLINK = "mpeihub://schedule"
        const val SCHEDULE_REQUEST_CODE = 2001
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
