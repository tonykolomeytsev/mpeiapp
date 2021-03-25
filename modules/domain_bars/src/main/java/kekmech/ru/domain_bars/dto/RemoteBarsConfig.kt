package kekmech.ru.domain_bars.dto

data class RemoteBarsConfig(
    val loginLink: String,
    val studentListLink: String,
    val js: JsKit
)

data class JsKit(
    val extractMarks: String,
    val changeSemester: String
)