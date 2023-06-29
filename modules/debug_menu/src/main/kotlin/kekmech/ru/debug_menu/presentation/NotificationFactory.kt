package kekmech.ru.debug_menu.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import kekmech.ru.debug_menu.R

internal class NotificationFactory {

    fun create(context: Context, pendingIntent: PendingIntent): Notification {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_debug_menu_24)
            .setContentTitle("MpeiX Debug Menu")
            .setContentText("Tap to open the debug menu")
            .setTicker("MpeiX debug menu available")
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)

        return builder.build()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private companion object {

        const val CHANNEL_ID = "mpeix_debug_menu"
        const val CHANNEL_NAME = "MpeiX Debug Menu"
        const val CHANNEL_DESCRIPTION = "MpeiX Debug Menu Notification"
    }
}
