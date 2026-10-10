package kekmech.ru.feature_bars_impl.data.network

import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.zip.GZIPInputStream

/**
 * Compatibility shim for 2FA page parsing inside lib_bars 0.4.1.
 *
 * lib_bars 0.4.1 strictly detects 2FA providers with:
 * 1) defaultProvider regex:
 *    `(?<!onclick=")\baf2_code_send\(\s*'btnSend',\s*'/bars_web/Auth/JSON_SendAF2_Code',\s*'(\d+)'\s*\)`
 *    - Expects exact button ID `'btnSend'`
 *    - Expects exactly 3 arguments (no 4th argument)
 *    - Must match an ID present in `TwoFactorProvider` enum: 1 (TG), 2 (VK), 3 (MAX).
 * 2) availableProviders string checks:
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')` (MAX)
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')` (VK)
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')` (TG)
 *
 * Current bars.mpei.ru (as of Oct 2026):
 * - Uses provider ID 5 (TOTP / одноразовый код, 6 digits) and provider ID 3 (MAX, 4 digits).
 * - Calls `af2_code_send` with 4 arguments: (btnId, url, tid, len).
 * - On page load, line in `<script>` calls: `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');`
 * - If provider 5 is left unmapped or with 4 arguments, lib_bars regex either fails or extracts '5' (not in enum),
 *   throwing `IllegalStateException("Unable to find two factor provider!")`.
 *
 * This interceptor:
 * 1) Detects the exact 2FA method (TOTP, MAX, Telegram, VK, Email) from the page attributes (data-text, tid, len)
 *    and stores it in BarsTwoFactorSessionHolder.
 * 2) On outgoing requests to `JSON_SendAF2_Code?tid=1`, rewrites `tid` to `5` so TOTP / Email resend works seamlessly.
 * 3) Normalizes all in-page `af2_code_send` calls:
 *    - Strips 4th argument.
 *    - Normalizes button IDs to `'btnSend'`.
 *    - Maps provider ID 5 to 1 (legacy slot in lib_bars).
 *    - Maps provider ID 3 to 3 (TwoFactorProvider.MAX).
 * 4) Appends a hidden shim ensuring all legacy providers are visible for availableProviders checks.
 * 5) If an error page like "Пользователь не найден" is returned without the expected login form placeholder,
 *    ensures "Введите пароль" is present so lib_bars classifies it as WrongCredentials instead of throwing.
 */
internal class BarsTwoFactorCompatInterceptor : Interceptor {

    private val fourArgCallRegex = Regex(
        """af2_code_send\(\s*['"][^'"]*['"]\s*,\s*['"]([^'"]*)['"]\s*,\s*['"](\d+)['"]\s*,\s*['"][^'"]*['"]\s*\)""",
    )

    private val threeArgCallRegex = Regex(
        """af2_code_send\(\s*['"][^'"]*['"]\s*,\s*['"]([^'"]*)['"]\s*,\s*['"](\d+)['"]\s*\)""",
    )

    private val scriptCallRegex = Regex(
        """(?<!onclick=[\"'])\baf2_code_send\(\s*['"]([^'"]*)['"]\s*,\s*['"]([^'"]*)['"]\s*,\s*['"](\d+)['"](?:\s*,\s*['"](\d+)['"])?\s*\)""",
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url

        // Outgoing request rewrite: TOTP / Email is mapped to legacy ID 1 in lib_bars, but BARS expects tid=5
        val actualRequest = if (
            (url.encodedPath == "/bars_web/Auth/JSON_SendAF2_Code" || url.encodedPath.endsWith("JSON_SendAF2_Code")) &&
            url.queryParameter("tid") == "1"
        ) {
            val rewrittenUrl = url.newBuilder()
                .setQueryParameter("tid", "5")
                .build()
            request.newBuilder().url(rewrittenUrl).build()
        } else {
            request
        }

        val response = chain.proceed(actualRequest)

        val body = response.body ?: return response
        val contentType = body.contentType()
        val mediaType = contentType?.toString()?.lowercase().orEmpty()
        val isHtmlOrTextOrJson = mediaType.isEmpty() ||
            mediaType.contains("text") ||
            mediaType.contains("html") ||
            mediaType.contains("xhtml") ||
            mediaType.contains("json")

        if (!isHtmlOrTextOrJson) return response

        val rawBytes = body.bytes()
        val isGzipped = response.header("Content-Encoding")?.lowercase() == "gzip"
        val text = if (isGzipped) {
            runCatching {
                GZIPInputStream(rawBytes.inputStream()).readBytes().toString(Charsets.UTF_8)
            }.getOrElse { return response.rebuildWith(rawBytes, contentType, stripEncoding = false) }
        } else {
            rawBytes.toString(Charsets.UTF_8)
        }

        // Capture server response message for JSON_SendAF2_Code
        val isSendCodeResponse = (url.encodedPath == "/bars_web/Auth/JSON_SendAF2_Code" || url.encodedPath.endsWith("JSON_SendAF2_Code"))
        if (isSendCodeResponse && response.isSuccessful) {
            val msgRegex = Regex("""\"message\"\s*:\s*\"([^\"]+)\"""")
            msgRegex.find(text)?.groupValues?.get(1)?.let { serverMsg ->
                val prev = BarsTwoFactorSessionHolder.currentSession
                BarsTwoFactorSessionHolder.currentSession = (prev ?: BarsTwoFactorSession(
                    defaultProvider = CodeProvider.MAX,
                    availableProviders = listOf(CodeProvider.MAX),
                )).copy(lastServerMessage = serverMsg)
            }
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        if (text.contains(SHIM_MARKER)) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        val isLoginCodeResponse = url.encodedPath.endsWith("LoginCode") || url.encodedPath.contains("LoginCode")
        if (isLoginCodeResponse) {
            var fixedLoginCode = text
            if (text.contains("Не удалось определить данные кода подтверждения") &&
                !text.contains("Некорректный код подтверждения")
            ) {
                fixedLoginCode = "$fixedLoginCode\n<!-- $SHIM_MARKER --><div style=\"display:none\">Некорректный код подтверждения</div>"
            }
            if (fixedLoginCode != text) {
                return response.rebuildWith(
                    bytes = fixedLoginCode.toByteArray(Charsets.UTF_8),
                    contentType = contentType,
                    stripEncoding = isGzipped,
                )
            }
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        val is2faPage = text.contains("af2_code_send") ||
            text.contains("JSON_SendAF2_Code") ||
            text.contains("AF2_Code")

        val isLoginFailurePage = (text.contains("Пользователь не найден") ||
            text.contains("Неверный логин") ||
            text.contains("Неверный пароль")) &&
            !text.contains("Введите пароль")

        if (!is2faPage && !isLoginFailurePage) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        if (is2faPage) {
            parseAndStoreSession(text)
        }

        var fixed = text

        if (isLoginFailurePage) {
            fixed = "$fixed\n<!-- $SHIM_MARKER --><div style=\"display:none\">Введите пароль</div>"
        }

        if (is2faPage) {
            val shim = buildString {
                append("\n<div id=\"$SHIM_MARKER\" style=\"display:none\">\n")
                // Legacy signatures for lib_bars regex and availableProviders checks
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')\n")
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')\n")
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')\n")
                append("</div>")
            }

            fixed = fixed
                .replace(fourArgCallRegex) {
                    val urlArg = it.groupValues[1]
                    val mappedId = mapProviderId(it.groupValues[2])
                    "af2_code_send('btnSend', '$urlArg', '$mappedId')"
                }
                .replace(threeArgCallRegex) {
                    val urlArg = it.groupValues[1]
                    val mappedId = mapProviderId(it.groupValues[2])
                    "af2_code_send('btnSend', '$urlArg', '$mappedId')"
                }
                .replace("onclick=\"af2_code_send", "onclick=\" af2_code_send")
                .replace("onclick='af2_code_send", "onclick=' af2_code_send")
                .plus(shim)
        }

        if (fixed == text) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        return response.rebuildWith(
            bytes = fixed.toByteArray(Charsets.UTF_8),
            contentType = contentType,
            stripEncoding = isGzipped,
        )
    }

    private fun parseAndStoreSession(html: String) {
        val scriptMatch = scriptCallRegex.find(html) ?: return
        val defaultBtnId = scriptMatch.groupValues[1]
        val defaultTid = scriptMatch.groupValues[3]
        val defaultLen = scriptMatch.groupValues.getOrNull(4)?.ifEmpty { "6" } ?: "6"

        val buttons = parseButtons(html)
        val defaultButton = buttons.find { it.id == defaultBtnId } ?: buttons.find { it.tid == defaultTid }
        val defaultProvider = resolveProvider(defaultTid, defaultLen, defaultButton?.dataText)

        val availableProviders = buttons
            .map { resolveProvider(it.tid, it.len, it.dataText) }
            .plus(defaultProvider)
            .distinct()

        val isTotp = defaultProvider == CodeProvider.TOTP

        BarsTwoFactorSessionHolder.currentSession = BarsTwoFactorSession(
            defaultProvider = defaultProvider,
            activeProvider = defaultProvider,
            availableProviders = availableProviders,
            codeLength = defaultLen.toIntOrNull() ?: 6,
            isTotp = isTotp,
            isTotpInitialized = false,
        )
    }

    private data class ButtonInfo(
        val id: String,
        val tid: String,
        val len: String,
        val dataText: String,
    )

    private fun parseButtons(html: String): List<ButtonInfo> {
        val aTagRegex = Regex("""<a\b([^>]*)>""", RegexOption.IGNORE_CASE)
        val idRegex = Regex("""\bid=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        val dataTextRegex = Regex("""\bdata-text=["']([^"']*)["']""", RegexOption.IGNORE_CASE)
        val callRegex = Regex("""af2_code_send\(\s*['"]([^'"]*)['"]\s*,\s*['"]([^'"]*)['"]\s*,\s*['"](\d+)['"](?:\s*,\s*['"](\d+)['"])?\s*\)""", RegexOption.IGNORE_CASE)

        val results = mutableListOf<ButtonInfo>()
        for (match in aTagRegex.findAll(html)) {
            val tagAttrs = match.groupValues[1]
            val callMatch = callRegex.find(tagAttrs) ?: continue
            val id = idRegex.find(tagAttrs)?.groupValues?.get(1).orEmpty()
            val dataText = dataTextRegex.find(tagAttrs)?.groupValues?.get(1).orEmpty()
            val tid = callMatch.groupValues[3]
            val len = callMatch.groupValues[4].ifEmpty { "4" }
            results.add(ButtonInfo(id = id, tid = tid, len = len, dataText = dataText))
        }
        return results
    }

    private fun resolveProvider(tid: String, len: String?, dataText: String?): CodeProvider {
        val text = dataText?.lowercase().orEmpty()
        return when {
            text.contains("одноразов") || text.contains("totp") || text.contains("authenticator") -> CodeProvider.TOTP
            text.contains("почт") || text.contains("email") || text.contains("e-mail") -> CodeProvider.EMAIL
            text.contains("макс") || text.contains("max") || tid == "3" -> CodeProvider.MAX
            text.contains("telegram") || text.contains("телеграм") || text.contains("тг") -> CodeProvider.TG
            text.contains("vk") || text.contains("вконтакт") || tid == "2" -> CodeProvider.VK
            tid == "5" -> if (len == "6") CodeProvider.TOTP else CodeProvider.EMAIL
            tid == "1" -> CodeProvider.TG
            else -> CodeProvider.MAX
        }
    }

    private fun mapProviderId(rawId: String): String =
        when (rawId.trim()) {
            "5" -> "1" // Mapped to 1 (legacy TG enum slot in lib_bars)
            "1" -> "1" // TG
            "2" -> "2" // VK
            "3" -> "3" // MAX
            else -> "3" // Fallback to MAX
        }

    private fun Response.rebuildWith(
        bytes: ByteArray,
        contentType: MediaType?,
        stripEncoding: Boolean,
    ): Response {
        val builder = newBuilder()
            .removeHeader("Content-Length")
            .body(bytes.toResponseBody(contentType))
        if (stripEncoding) {
            builder.removeHeader("Content-Encoding")
        }
        return builder.build()
    }

    private companion object {
        const val SHIM_MARKER = "lib_bars_compat_shim"
    }
}
