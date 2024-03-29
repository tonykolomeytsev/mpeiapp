package kekmech.ru.ext_kotlin

import android.util.Base64
import android.util.Base64.URL_SAFE

fun String.toBase64() = Base64.encode(toByteArray(), URL_SAFE).decodeToString()

fun String.fromBase64() = Base64.decode(toByteArray(), URL_SAFE).decodeToString()

fun String.capitalizeSafe(): String =
    replaceFirstChar { it.uppercase() }
