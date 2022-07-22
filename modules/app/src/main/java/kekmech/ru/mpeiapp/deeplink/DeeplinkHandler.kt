package kekmech.ru.mpeiapp.deeplink

import android.net.Uri

abstract class DeeplinkHandler(
    val path: String
) {

    abstract fun handle(deeplink: Uri)
}
