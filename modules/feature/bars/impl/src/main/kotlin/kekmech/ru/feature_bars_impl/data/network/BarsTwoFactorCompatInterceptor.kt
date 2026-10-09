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
 * The current bars.mpei.ru markup renders these calls with a 4th argument (code length)
 * and only inside `onclick="..."` attributes:
 * ```
 * <a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" ...>
 * ```
 * so the library regex matches nothing, no 2FA provider is detected and the login
 * screen shows an endless loading state after correct credentials.
 *
 * This interceptor rewrites such HTML pages on the fly:
 *  - strips the 4th argument from `af2_code_send(...)` calls;
 *  - inserts a space after `onclick="` so the calls become visible to the library regex.
 *
 * It also forces `Accept-Encoding: gzip` on BARS requests so the body can always be
 * decoded and rewritten here (the server may otherwise answer with br/zstd, which
 * cannot be re-encoded on the client).
 *
 * Pages without the 2FA marker are passed through byte-identical.
 */
internal class BarsTwoFactorCompatInterceptor : Interceptor {

    private val fourArgCallRegex = Regex(
        """af2_code_send\(\s*('btnSend\d*'\s*,\s*'[^']*'\s*,\s*'\d+')\s*,\s*'\d+'\s*\)""",
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept-Encoding", "gzip")
            .build()
        val response = chain.proceed(request)

        val body = response.body ?: return response
        val contentType = body.contentType()
        if (contentType == null || contentType.type != "text") return response

        val rawBytes = body.bytes()
        val isGzipped = response.header("Content-Encoding")?.lowercase() == "gzip"
        val text = if (isGzipped) {
            runCatching {
                GZIPInputStream(rawBytes.inputStream()).readBytes().toString(Charsets.UTF_8)
            }.getOrElse { return response.rebuildWith(rawBytes, contentType, stripEncoding = false) }
        } else {
            rawBytes.toString(Charsets.UTF_8)
        }

        if (!text.contains(TWO_FACTOR_MARKER)) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }

        val fixed = text
            .replace(fourArgCallRegex) { "af2_code_send(${it.groupValues[1]})" }
            .replace("onclick=\"$TWO_FACTOR_MARKER", "onclick=\" $TWO_FACTOR_MARKER")

        if (fixed == text) {
            return response.rebuildWith(rawBytes, contentType, stripEncoding = false)
        }
        return response.rebuildWith(
            bytes = fixed.toByteArray(Charsets.UTF_8),
            contentType = contentType,
            stripEncoding = true,
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
        const val TWO_FACTOR_MARKER = "af2_code_send"
    }
}
