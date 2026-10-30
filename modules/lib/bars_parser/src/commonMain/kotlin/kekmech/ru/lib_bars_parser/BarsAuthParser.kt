package kekmech.ru.lib_bars_parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document

private val AF2_CALL = Regex(
    """af2_code_send\(\s*['"]([^'"]*)['"]\s*,\s*['"]([^'"]*)['"]\s*,\s*['"](\d+)['"](?:\s*,\s*['"](\d+)['"])?\s*\)""",
)
private val JSON_SUCCESS = Regex(""""success"\s*:\s*(true|false)""")
private val JSON_MESSAGE = Regex(""""message"\s*:\s*"((?:[^"\\]|\\.)*)"""")
private val JSON_ESCAPE = Regex("""\\(u[0-9a-fA-F]{4}|["\\/bfnrt])""")

/**
 * Parser of the pages of the sign-in: the form with the login and the password and the page of the second factor.
 * Like [BarsHtmlParser] it reads text and does nothing else: the requests and the cookies are somebody else's job.
 */
object BarsAuthParser {

    /**
     * The form of the sign-in page.
     * @throws BarsParseException if there is no form with a password input in [html]
     */
    fun parseLoginForm(html: String): BarsLoginForm {
        val document = Ksoup.parse(html)
        val password = document.selectFirst("input[type=password]")
            ?: throw BarsParseException("There is no password input, it is not the sign-in page")
        val form = password.parents().firstOrNull { it.tagName() == "form" }
            ?: throw BarsParseException("The password input is not in a form")
        val account = form.selectFirst("input[type=text][name]")
            ?: throw BarsParseException("There is no login input in the sign-in form")

        return BarsLoginForm(
            action = form.attr("action"),
            method = form.attr("method").ifEmpty { "get" }.lowercase(),
            accountField = account.attr("name"),
            passwordField = password.attr("name"),
            rememberMeField = form.selectFirst("input[type=checkbox][name]")?.attr("name"),
            hiddenFields = form.select("input[type=hidden][name]").associate { it.attr("name") to it.attr("value") },
        )
    }

    /**
     * The page that asks for a code: the way that it starts with and the other ways to get a code.
     * @throws BarsParseException if there is no `af2_code_send` on the page
     */
    fun parseTwoFactorPage(html: String): BarsTwoFactorPage {
        val document = Ksoup.parse(html)

        val buttons = document.select("[onclick*=af2_code_send]").mapNotNull { element ->
            AF2_CALL.find(element.attr("onclick"))?.let { call ->
                val caption = element.attr("data-text").ifEmpty { element.text() }
                Triple(element.id(), call, normalizeBarsText(caption))
            }
        }
        val scriptCall = AF2_CALL.find(document.select("script").joinToString("\n") { it.data() })
        if (buttons.isEmpty() && scriptCall == null) {
            throw BarsParseException("There is no af2_code_send on the page, it is not the second factor page")
        }

        val providers = buttons.map { (id, call, caption) ->
            provider(buttonId = id, tid = call.groupValues[3], length = call.groupValues[4].ifEmpty { "4" }, caption = caption)
        }
        val default = if (scriptCall != null) {
            val tid = scriptCall.groupValues[3]
            val buttonId = scriptCall.groupValues[1]
            val caption = buttons.firstOrNull { it.first == buttonId }?.third
                ?: buttons.firstOrNull { it.second.groupValues[3] == tid }?.third
                .orEmpty()
            provider(buttonId, tid, scriptCall.groupValues[4].ifEmpty { "6" }, caption)
        } else {
            providers.first()
        }

        return BarsTwoFactorPage(
            default = default,
            providers = (listOf(default) + providers).distinctBy { it.tid },
            sendCodeUrl = (scriptCall ?: buttons.first().second).groupValues[2],
            error = findError(document),
        )
    }

    /** The error that a page shows under the form ("Пользователь не найден", a wrong code), null if there is none. */
    fun parseFormError(html: String): String? = findError(Ksoup.parse(html))

    /** The answer of the request that sends a code. Anything that is not understood stays null. */
    fun parseSendCodeResult(json: String): BarsSendCodeResult = BarsSendCodeResult(
        success = JSON_SUCCESS.find(json)?.groupValues?.get(1)?.toBooleanStrictOrNull(),
        message = JSON_MESSAGE.find(json)?.groupValues?.get(1)?.let(::decodeJsonEscapes),
    )

    private fun findError(document: Document): String? =
        document.select(".alert-danger, .field-validation-error, .validation-summary-errors")
            .map { normalizeBarsText(it.text()) }
            .firstOrNull { it.isNotEmpty() }

    private fun provider(buttonId: String, tid: String, length: String, caption: String) = BarsTwoFactorProvider(
        kind = resolveKind(tid = tid, length = length, caption = caption),
        buttonId = buttonId,
        tid = tid,
        codeLength = length.toIntOrNull() ?: 4,
        caption = caption,
    )

    /** What BARS means by a `tid`: it is told by the caption first, because 5 is used for both a TOTP and an e-mail. */
    private fun resolveKind(tid: String, length: String, caption: String): BarsTwoFactorKind {
        val text = caption.lowercase()
        return when {
            "почт" in text || "email" in text || "e-mail" in text -> BarsTwoFactorKind.EMAIL
            "одноразов" in text || "totp" in text || "authenticator" in text -> BarsTwoFactorKind.TOTP
            "макс" in text || "max" in text || tid == "3" -> BarsTwoFactorKind.MAX
            "telegram" in text || "телеграм" in text -> BarsTwoFactorKind.TELEGRAM
            "vk" in text || "вконтакт" in text || tid == "2" -> BarsTwoFactorKind.VK
            tid == "5" -> if (length == "6") BarsTwoFactorKind.TOTP else BarsTwoFactorKind.EMAIL
            tid == "1" -> BarsTwoFactorKind.TELEGRAM
            else -> BarsTwoFactorKind.MAX
        }
    }

    /** The text was cut out of a JSON string by a regular expression: `'MAX'` has to become `'MAX'`. */
    private fun decodeJsonEscapes(raw: String): String = JSON_ESCAPE.replace(raw) { match ->
        val code = match.groupValues[1]
        when (code[0]) {
            'u' -> code.substring(1).toInt(16).toChar().toString()
            'b' -> "\b"
            'f' -> "\u000C"
            'n' -> "\n"
            'r' -> "\r"
            't' -> "\t"
            else -> code // \" \\ \/
        }
    }
}
