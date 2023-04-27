package kekmech.ru.mpeiapp.deeplink

import android.net.Uri
import android.os.Handler
import android.os.Looper

class DeeplinkHandlersProcessor(
    private val handlers: List<DeeplinkHandler>,
) {

    private val uiHandler = Handler(Looper.getMainLooper())

    fun processDeeplink(deeplink: Uri?) {
        if (deeplink != null) {
            val necessaryHandler = handlers.find { it.path == deeplink.host } ?: return
            uiHandler.post { necessaryHandler.handle(deeplink) }
        }
    }
}
