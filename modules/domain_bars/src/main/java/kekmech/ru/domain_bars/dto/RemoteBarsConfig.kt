package kekmech.ru.domain_bars.dto

import android.util.Base64

data class RemoteBarsConfig(
    val loginUrl: String,
    val studentListUrl: String,
    val marksListUrl: String,
    val logoutUrl: String,
    val js: JsKit
)

data class JsKit(
    val extractDataEncoded: String,
    val changeSemesterEncoded: String
) {
    val extractDataDecoded: String get() =
        String(Base64.decode(extractDataEncoded.toByteArray(), Base64.NO_WRAP))

    val changeSemesterDecoded: String get() =
        String(Base64.decode(changeSemesterEncoded.toByteArray(), Base64.NO_WRAP))
}