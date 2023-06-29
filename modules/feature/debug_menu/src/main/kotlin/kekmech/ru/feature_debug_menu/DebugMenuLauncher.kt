package kekmech.ru.feature_debug_menu

import android.content.Context
import android.content.Intent
import android.os.Build
import kekmech.ru.feature_debug_menu.presentation.DebugMenuService
import timber.log.Timber

object DebugMenuLauncher {

    fun launch(context: Context) {
        Timber.d("Launching debug menu...")
        val intent = Intent(context, DebugMenuService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
