package kekmech.ru.feature_schedule_impl.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kekmech.ru.feature_schedule_impl.R
import timber.log.Timber
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
            .setSmallIcon(android.R.drawable.ic_dialog_info) // System icon
            .setContentTitle(getNotificationTitle(changes.size))
            .setContentText(getNotificationPreview(changes))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getDetailedChangesText(changes))
            )
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
                "Изменения расписания",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления об изменениях в расписании пар"
                enableVibration(true)
                enableLights(true)
            }
            
            val systemNotificationManager = context.getSystemService(NotificationManager::class.java)
            systemNotificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Create intent to open schedule screen when notification is tapped
     */
    private fun createContentIntent(): PendingIntent {
        // TODO: Replace with actual intent to schedule screen
        val intent = Intent(context, Class.forName("kekmech.ru.mpeix.MainActivity"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    /**
     * Get notification title based on number of changes
     */
    private fun getNotificationTitle(count: Int): String {
        return when {
            count == 1 -> "Изменение в расписании"
            count < 5 -> "Изменения в расписании"
            else -> "Много изменений в расписании"
        }
    }
    
    /**
     * Get short preview text for notification
     */
    private fun getNotificationPreview(changes: List<ScheduleChange>): String {
        val change = changes.first()
        val dateStr = formatDate(change.date)
        
        return when {
            changes.size == 1 -> "$dateStr: ${change.getDescription()}"
            changes.size <= 3 -> "У вас ${changes.size} изменения на этой неделе"
            else -> "У вас ${changes.size} изменений на этой неделе"
        }
    }
    
    /**
     * Get detailed text with all changes for expanded notification
     */
    private fun getDetailedChangesText(changes: List<ScheduleChange>): String {
        return changes
            .take(MAX_CHANGES_IN_NOTIFICATION) // Limit to avoid too long notification
            .groupBy { it.date }
            .entries
            .sortedBy { it.key }
            .joinToString("\n\n") { (date, dayChanges) ->
                val dateStr = formatDate(date)
                val changesStr = dayChanges.joinToString("\n") { "• ${it.getDescription()}" }
                "$dateStr:\n$changesStr"
            }
            .let { text ->
                if (changes.size > MAX_CHANGES_IN_NOTIFICATION) {
                    text + "\n\n... и ещё ${changes.size - MAX_CHANGES_IN_NOTIFICATION}"
                } else {
                    text
                }
            }
    }
    
    /**
     * Format date for display
     */
    private fun formatDate(date: java.time.LocalDate): String {
        val today = java.time.LocalDate.now()
        val tomorrow = today.plusDays(1)
        
        return when (date) {
            today -> "Сегодня"
            tomorrow -> "Завтра"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("d MMMM (EEE)", Locale("ru"))
                date.format(formatter)
            }
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "schedule_changes"
        private const val NOTIFICATION_ID = 1001
        private const val MAX_CHANGES_IN_NOTIFICATION = 10
    }
}
