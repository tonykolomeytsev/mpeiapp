package kekmech.ru.domain_bars.dto

import android.util.Base64

data class RemoteBarsConfig(
    val loginLink: String,
    val studentListLink: String,
    val js: JsKit
)

data class JsKit(
    val extractDataEncoded: String,
    val changeSemesterEncoded: String
) {
    val extractDataDecoded: String by lazy(LazyThreadSafetyMode.NONE) {
        String(Base64.decode(extractDataEncoded.toByteArray(), Base64.NO_WRAP))
    }
    val changeSemesterDecoded: String by lazy(LazyThreadSafetyMode.NONE) {
        String(Base64.decode(changeSemesterEncoded.toByteArray(), Base64.NO_WRAP))
    }
}