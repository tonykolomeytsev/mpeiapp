package kekmech.ru.common_android

import android.util.Base64
import android.util.Base64.URL_SAFE

fun String.toBase64() = Base64.encode(toByteArray(), URL_SAFE).decodeToString()

fun String.fromBase64() = Base64.decode(toByteArray(), URL_SAFE).decodeToString()