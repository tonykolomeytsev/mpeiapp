package kekmech.ru.ext_android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE

public fun Context.copyToClipboard(text: String, label: String = ""): Boolean =
    runCatching {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }.isSuccess
