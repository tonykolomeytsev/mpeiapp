package kekmech.ru.feature_bars_impl.data.network

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.zip.GZIPInputStream

/**
 * Compatibility shim for the 2FA page parsing inside lib_bars 0.4.1.
 *
 * lib_bars detects available 2FA providers with a regex that expects calls in the form
 * ```
 * af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')
 * ```
 * located NOT directly inside an `onclick="` attribute.
 *
 * Current bars.mpei.ru markup renders these calls with distinct button IDs (e.g. 'btnSend1',
 * 'btnSend2'), sometimes a 4th argument (code length), and strictly inside `onclick="..."`:
 * ```
 * <a id="btnSend"  href="#" onclick="af2_code_send('btnSend',  '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" ...>
 * <a id="btnSend2" href="#" onclick="af2_code_send('btnSend2', '/bars_web/Auth/JSON_SendAF2_Code', '2', '4');" ...>
 * <a id="btnSend1" href="#" onclick="af2_code_send('btnSend1', '/bars_web/Auth/JSON_SendAF2_Code', '1', '4');" ...>
 * ```
 *
 * lib_bars strictly checks:
 * 1) defaultProvider via:
 *    `(?<!onclick=")\baf2_code_send\(\s*'btnSend',\s*'/bars_web/Auth/JSON_SendAF2_Code',\s*'(\d+)'\s*\)`
 *    (fails if button ID is not 'btnSend' or if call is inside onclick=" without preceding non-onclick call).
 * 2) availableProviders via exact string `contains`:
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')` (MAX)
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')` (VK)
 *    `af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')` (TG)
 *    (fails if button IDs differ from 'btnSend' or if 4th argument is present).
 *
 * If no default provider is found, lib_bars throws IllegalStateException("Unable to find two factor provider!"),
 * causing login failure on valid credentials instead of transitioning to the 2FA screen.
 *
 * This interceptor:
 * 1) Normalizes any in-page `af2_code_send` calls so button IDs become 'btnSend', 4th arguments are stripped,
 *    and whitespace after `onclick="` is ensured.
 * 2) Appends a synthetic hidden shim with all three providers outside `onclick="` with the detected default provider first,
 *    guaranteeing that lib_bars regex and contains checks always succeed.
 * 3) If an error page like "Пользователь не найден" is returned without the expected login form placeholder,
 *    ensures "Введите пароль" is present so lib_bars classifies it as WrongCredentials instead of throwing.
 */
internal class BarsTwoFactorCompatInterceptor : Interceptor {

    private val fourArgCallRegex = Regex(
        """af2_code_send\(\s*['"][^'"]*['"]\s*,\s*('[^']*')\s*,\s*('(?:\d+)')\s*,\s*'[^\']*'\s*\)""",
    )

    private val threeArgCallRegex = Regex(
        """af2_code_send\(\s*['"][^'"]*['"]\s*,\s*('[^']*')\s*,\s*('(?:\d+)')\s*\)""",
    )

    private val providerIdRegex = Regex(
        """af2_code_send\s*\([^)]*['"]([123])['"]""",
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val body = response.body ?: return response
        val contentType = body.contentType()
        val mediaType = contentType?.toString()?.lowercase().orEmpty()
        val isHtmlOrText = mediaType.isEmpty() ||
            mediaType.contains("text") ||
            mediaType.contains("html") ||
            mediaType.contains("xhtml")

        if (!isHtmlOrText) return response

        val rawBytes = body.bytes()
        val isGzipped = response.header("Content-Encoding")?.lowercase() == "gzip"
        val text = if (isGzipped) {
            runCatching {
                GZIPInputStream(rawBytes.inputStream()).readBytes().toString(Charsets.UTF_8)
            }.getOrElse { return response.rebuildWith(rawBytes, contentType, stripEncoding = false) }
        } else {
            rawBytes.toString(Charsets.UTF_8)
        }

        if (text.contains(SHIM_MARKER)) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        val is2faPage = text.contains("af2_code_send") ||
            text.contains("JSON_SendAF2_Code") ||
            text.contains("AF2_Code") ||
            text.contains("LoginCode")

        val isLoginFailurePage = (text.contains("Пользователь не найден") ||
            text.contains("Неверный логин") ||
            text.contains("Неверный пароль")) &&
            !text.contains("Введите пароль")

        if (!is2faPage && !isLoginFailurePage) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        var fixed = text

        if (isLoginFailurePage) {
            fixed = "$fixed\n<!-- $SHIM_MARKER --><div style=\"display:none\">Введите пароль</div>"
        }

        if (is2faPage) {
            val defaultProviderId = providerIdRegex.find(text)?.groupValues?.get(1)
                ?: when {
                    text.contains("MAX", ignoreCase = true) -> "3"
                    text.contains("Telegram", ignoreCase = true) || text.contains("ТГ", ignoreCase = true) -> "1"
                    text.contains("VK", ignoreCase = true) || text.contains("ВКонтакте", ignoreCase = true) -> "2"
                    else -> "3"
                }

            val shim = buildString {
                append("\n<div id=\"$SHIM_MARKER\" style=\"display:none\">\n")
                // lib_bars regex takes the first match outside onclick=" as defaultProvider
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '$defaultProviderId')\n")
                // lib_bars contains checks for availableProviders:
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')\n")
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')\n")
                append("af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')\n")
                append("</div>")
            }

            fixed = fixed
                .replace(fourArgCallRegex) { "af2_code_send('btnSend', ${it.groupValues[1]}, ${it.groupValues[2]})" }
                .replace(threeArgCallRegex) { "af2_code_send('btnSend', ${it.groupValues[1]}, ${it.groupValues[2]})" }
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
