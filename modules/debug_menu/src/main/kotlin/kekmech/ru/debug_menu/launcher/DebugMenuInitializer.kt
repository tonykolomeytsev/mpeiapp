package kekmech.ru.debug_menu.launcher

import android.content.Context
import android.content.Intent
import android.os.Build
import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.debug_menu.presentation.DebugMenuService
import timber.log.Timber

internal class DebugMenuInitializer : MainActivityLifecycleObserver {

    override fun onCreate(context: Context) {
        Timber.d("Launching debug menu...")
        val intent = Intent(context, DebugMenuService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
