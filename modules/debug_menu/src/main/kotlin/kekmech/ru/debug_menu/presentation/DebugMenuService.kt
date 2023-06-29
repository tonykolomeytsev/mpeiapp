package kekmech.ru.debug_menu.presentation

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.random.Random

internal class DebugMenuService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val activityIntent = Intent(this, DebugMenuActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationFactory().create(this, pendingIntent)
        startForeground(Random.nextInt(), notification)
        return START_STICKY
    }
}
