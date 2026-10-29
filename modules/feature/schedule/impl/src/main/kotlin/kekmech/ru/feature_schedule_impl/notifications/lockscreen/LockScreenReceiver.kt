package kekmech.ru.feature_schedule_impl.notifications.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.context.GlobalContext

/** Fired by the alarm when the current / next class changes */
internal class LockScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        GlobalContext.getOrNull()?.get<LockScreenScheduleController>()?.refreshNow()
    }

    companion object {
        const val ACTION = "kekmech.ru.mpeiapp.LOCK_SCREEN_SCHEDULE_REFRESH"
    }
}
