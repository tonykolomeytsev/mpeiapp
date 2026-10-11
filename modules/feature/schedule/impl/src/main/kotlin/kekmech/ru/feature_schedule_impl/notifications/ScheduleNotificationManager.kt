package kekmech.ru.feature_schedule_impl.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kekmech.ru.res_icons.R.drawable as ResIcons
import kekmech.ru.res_strings.R.string as Strings
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Manages notifications for schedule changes
 */
internal class ScheduleNotificationManager(
    private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    /**
     * Show notification about schedule changes
     */
    fun showScheduleChangesNotification(changes: List<ScheduleChange>) {
        if (changes.isEmpty()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(ResIcons.ic_notification_schedule_24)
            .setContentTitle(getNotificationTitle(changes.size))
            .setContentText(getNotificationPreview(changes))
            .setStyle(NotificationCompat.BigTextStyle().bigText(getDetailedChangesText(changes)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setAutoCancel(true)
            .setContentIntent(createContentIntent())
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID, notification)
            Timber.d("ScheduleNotificationManager: Notification sent")
        } catch (e: SecurityException) {
            Timber.e(e, "ScheduleNotificationManager: Permission denied")
        }
    }

    /**
     * Create notification channel (Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(Strings.schedule_changes_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(Strings.schedule_changes_channel_description)
                enableVibration(true)
                enableLights(true)
            }

            val systemNotificationManager = context.getSystemService(NotificationManager::class.java)
            systemNotificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Create intent to open the app when notification is tapped
     */
    private fun createContentIntent(): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getNotificationTitle(count: Int): String = context.getString(
        when {
            count == 1 -> Strings.schedule_changes_title_single
            count < 5 -> Strings.schedule_changes_title_few
            else -> Strings.schedule_changes_title_many
        }
    )

    private fun getNotificationPreview(changes: List<ScheduleChange>): String {
        val change = changes.first()
        return if (changes.size == 1) {
            "${formatDate(change.date)}: ${change.getDescription(context)}"
        } else {
            context.getString(Strings.schedule_changes_preview_many, changes.size)
        }
    }

    /**
     * Get detailed text with all changes for expanded notification
     */
    private fun getDetailedChangesText(changes: List<ScheduleChange>): String {
        val text = changes
            .sortedBy { it.date }
            .take(MAX_CHANGES_IN_NOTIFICATION)
            .groupBy { it.date }
            .entries
            .joinToString("\n\n") { (date, dayChanges) ->
                val changesStr = dayChanges.joinToString("\n") { "• ${it.getDescription(context)}" }
                "${formatDate(date)}:\n$changesStr"
            }
        val hidden = changes.size - MAX_CHANGES_IN_NOTIFICATION
        return if (hidden > 0) {
            text + "\n\n" + context.getString(Strings.schedule_changes_more, hidden)
        } else {
            text
        }
    }

    private fun formatDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> context.getString(Strings.schedule_changes_today)
            today.plusDays(1) -> context.getString(Strings.schedule_changes_tomorrow)
            else -> date.format(DateTimeFormatter.ofPattern("d MMMM (EEE)", Locale.getDefault()))
        }
    }

    private companion object {
        const val CHANNEL_ID = "schedule_changes"
        const val NOTIFICATION_ID = 1001
        const val MAX_CHANGES_IN_NOTIFICATION = 10
    }
}
