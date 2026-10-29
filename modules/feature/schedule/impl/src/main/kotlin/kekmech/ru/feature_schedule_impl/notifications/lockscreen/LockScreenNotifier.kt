package kekmech.ru.feature_schedule_impl.notifications.lockscreen

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
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_impl.domain.ClassTimeline
import timber.log.Timber
import java.time.format.DateTimeFormatter
import kekmech.ru.res_icons.R.drawable as ResIcons
import kekmech.ru.res_strings.R.string as Strings

/**
 * Persistent notification with the current and the next class. It is public on the lock screen
 * (`VISIBILITY_PUBLIC`), so it works on every firmware, also where the lock screen widgets are blocked.
 */
internal class LockScreenNotifier(
    private val context: Context,
) {

    // the language chosen in the app, not the one of the system
    private val localized get() = AppLocales.localized(context)

    fun show(timeline: ClassTimeline) {
        createChannel()
        val (title, text) = texts(timeline)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(ResIcons.ic_notification_schedule_24)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText(timeline, text)))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setContentIntent(openScheduleIntent())
            .build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS is not granted
            Timber.w(e, "LockScreenNotifier: permission denied")
        }
    }

    fun hide() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_TAG, NOTIFICATION_ID)
    }

    /** Title and one line of text, compact enough to be read on the lock screen */
    private fun texts(timeline: ClassTimeline): Pair<String, String> {
        val res = localized
        val current = timeline.current
        val next = timeline.next
        return when {
            current != null -> {
                val title = res.getString(Strings.lock_screen_title_now, current.name)
                val text = next?.let { res.getString(Strings.lock_screen_text_next, it.line()) }
                    ?: res.getString(Strings.lock_screen_text_until, current.time.end.format(TIME))
                title to listOf(current.details(), text).filter { it.isNotBlank() }.joinToString("\n")
            }

            next != null -> res.getString(
                Strings.lock_screen_title_next,
                next.time.start.format(TIME),
                next.name,
            ) to next.details()

            timeline.isOver -> res.getString(Strings.lock_screen_title_over) to ""
            else -> res.getString(Strings.lock_screen_title_empty) to ""
        }
    }

    private fun bigText(timeline: ClassTimeline, fallback: String): String =
        timeline.remaining
            .take(MAX_LINES)
            .joinToString("\n") { it.line() }
            .ifBlank { fallback }

    private fun Classes.line(): String =
        listOf("${time.start.format(TIME)}–${time.end.format(TIME)}", name, place.takeIf { it.isNotBlank() })
            .filterNotNull()
            .joinToString(" · ")

    private fun Classes.details(): String =
        listOf("${time.start.format(TIME)}–${time.end.format(TIME)}", place.takeIf { it.isNotBlank() })
            .filterNotNull()
            .joinToString(" · ")

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
                localized.getString(Strings.lock_screen_channel_name),
                // silent: no sound, no pop-up, only the entry in the shade and on the lock screen
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = localized.getString(Strings.lock_screen_channel_description)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(false)
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private companion object {
        const val CHANNEL_ID = "lock_screen_schedule"
        const val NOTIFICATION_TAG = "lock_screen_schedule"
        const val NOTIFICATION_ID = 3001
        const val SCHEDULE_DEEPLINK = "mpeihub://schedule"
        const val SCHEDULE_REQUEST_CODE = 3002
        const val MAX_LINES = 4
        val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
