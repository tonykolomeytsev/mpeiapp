package kekmech.ru.ext_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ArrayRes

public fun Context.getStringArray(@ArrayRes stringRes: Int): Array<String> =
    resources.getStringArray(stringRes)

@SuppressLint("UseKtx")
public fun Context.openLinkExternal(link: String) {
    openLinkExternal(Uri.parse(link))
}

public fun Context.openLinkExternal(link: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, link))
}
