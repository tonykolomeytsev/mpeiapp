package kekmech.ru.debug_menu.initializer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import kekmech.ru.debug_menu.presentation.DebugMenuActivity
import timber.log.Timber

internal class DebugMenuService : Service() {

    private var notification: Notification? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        Timber.d("Starting debug menu service...")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val activityIntent = Intent(this, DebugMenuActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE,
        )
        notification = NotificationFactory().create(this, pendingIntent)
        startForeground(DebugMenuServiceId, notification)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Timber.d("Stopping debug menu service...")
        stopSelf()
    }

    private companion object {

        const val DebugMenuServiceId = 1
    }
}
