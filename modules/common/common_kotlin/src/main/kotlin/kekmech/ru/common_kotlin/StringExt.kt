package kekmech.ru.common_kotlin

import android.util.Base64
import android.util.Base64.URL_SAFE

public fun String.toBase64(): String = Base64.encode(toByteArray(), URL_SAFE).decodeToString()

public fun String.fromBase64(): String = Base64.decode(toByteArray(), URL_SAFE).decodeToString()

public fun String.capitalizeSafe(): String =
    replaceFirstChar { it.uppercase() }
