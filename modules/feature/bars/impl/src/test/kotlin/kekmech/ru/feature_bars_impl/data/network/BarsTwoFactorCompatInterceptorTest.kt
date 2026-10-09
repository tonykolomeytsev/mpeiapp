package kekmech.ru.feature_bars_impl.data.network

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

internal class BarsTwoFactorCompatInterceptorTest : StringSpec({

    val interceptor = BarsTwoFactorCompatInterceptor()

    fun gzip(text: String): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).use { it.write(text.toByteArray(Charsets.UTF_8)) }
        return bos.toByteArray()
    }

    class FakeChain(
        private val request: Request,
        private val response: Response,
    ) : Interceptor.Chain {
        override fun request(): Request = request
        override fun proceed(request: Request): Response = response
        override fun connection() = null
        override fun call() = throw UnsupportedOperationException()
        override fun connectTimeoutMillis() = 0
        override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        override fun readTimeoutMillis() = 0
        override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        override fun writeTimeoutMillis() = 0
        override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
    }

    fun execute(html: String): Response {
        val chain = FakeChain(
            request = Request.Builder().url("https://bars.mpei.ru/bars_web/").build(),
            response = Response.Builder()
                .request(Request.Builder().url("https://bars.mpei.ru/bars_web/").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(html.toResponseBody("text/html; charset=utf-8".toMediaType()))
                .build(),
        )
        return interceptor.intercept(chain)
    }

    "intercept rewrites 2FA page with provider 5 (email) as default" {
        val html = """
            <html>
            <body>
            <a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');" data-text="Войти по одноразовому коду">
            <a id="btnSend3" href="#" onclick="af2_code_send('btnSend3', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" data-text="Отправить повторно через МАКС">
            </body>
            </html>
        """.trimIndent()

        val response = execute(html)
        val body = response.body?.string().orEmpty()

        // Provider 5 (email) should be mapped to provider 1 (legacy slot in lib_bars)
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')"
        // Provider 3 (MAX) remains provider 3
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')"
        body shouldContain "lib_bars_compat_shim"
        body shouldContain "onclick=\" af2_code_send"
    }

    "intercept rewrites 2FA page with telegram button first" {
        val html = """
            <html>
            <body>
            <a id="btnSend1" href="#" onclick="af2_code_send('btnSend1', '/bars_web/Auth/JSON_SendAF2_Code', '1', '4');" data-text="Telegram">
            <a id="btnSend2" href="#" onclick="af2_code_send('btnSend2', '/bars_web/Auth/JSON_SendAF2_Code', '2', '4');" data-text="VK">
            </body>
            </html>
        """.trimIndent()

        val response = execute(html)
        val body = response.body?.string().orEmpty()

        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')"
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')"
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')"
        body shouldContain "onclick=\" af2_code_send"
        body shouldNotContain "Введите пароль"
    }

    "intercept rewrites 2FA page with MAX button first" {
        val html = """
            <html>
            <body>
            <a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" data-text="MAX">
            <a id="btnSend2" href="#" onclick="af2_code_send('btnSend2', '/bars_web/Auth/JSON_SendAF2_Code', '2', '4');" data-text="VK">
            <a id="btnSend1" href="#" onclick="af2_code_send('btnSend1', '/bars_web/Auth/JSON_SendAF2_Code', '1', '4');" data-text="Telegram">
            </body>
            </html>
        """.trimIndent()

        val response = execute(html)
        val body = response.body?.string().orEmpty()

        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')"
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '2')"
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '1')"
    }

    "intercept injects password text when login fails without password prompt" {
        val html = """
            <html>
            <body>
            <div class="alert alert-danger">Пользователь не найден</div>
            </body>
            </html>
        """.trimIndent()

        val response = execute(html)
        val body = response.body?.string().orEmpty()

        body shouldContain "Введите пароль"
    }

    "intercept correctly decodes and rewrites gzipped responses" {
        val html = """
            <html>
            <body>
            <a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');">
            </body>
            </html>
        """.trimIndent()

        val gzipped = gzip(html)
        val chain = FakeChain(
            request = Request.Builder().url("https://bars.mpei.ru/bars_web/").build(),
            response = Response.Builder()
                .request(Request.Builder().url("https://bars.mpei.ru/bars_web/").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .header("Content-Encoding", "gzip")
                .body(gzipped.toResponseBody("text/html; charset=utf-8".toMediaType()))
                .build(),
        )

        val response = interceptor.intercept(chain)
        val body = response.body?.string().orEmpty()

        body shouldContain "lib_bars_compat_shim"
        body shouldContain "af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3')"
    }

    "intercept rewrites outgoing request tid=1 to tid=5 for Email" {
        var passedRequest: Request? = null
        val chain = object : Interceptor.Chain {
            override fun request(): Request = Request.Builder()
                .url("https://bars.mpei.ru/bars_web/Auth/JSON_SendAF2_Code?tid=1")
                .build()
            override fun proceed(request: Request): Response {
                passedRequest = request
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("{}".toResponseBody("application/json".toMediaType()))
                    .build()
            }
            override fun connection() = null
            override fun call() = throw UnsupportedOperationException()
            override fun connectTimeoutMillis() = 0
            override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
            override fun readTimeoutMillis() = 0
            override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
            override fun writeTimeoutMillis() = 0
            override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        }

        interceptor.intercept(chain)
        passedRequest?.url?.queryParameter("tid") shouldBe "5"
    }

    "intercept keeps outgoing request tid=3 untouched for MAX" {
        var passedRequest: Request? = null
        val chain = object : Interceptor.Chain {
            override fun request(): Request = Request.Builder()
                .url("https://bars.mpei.ru/bars_web/Auth/JSON_SendAF2_Code?tid=3")
                .build()
            override fun proceed(request: Request): Response {
                passedRequest = request
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("{}".toResponseBody("application/json".toMediaType()))
                    .build()
            }
            override fun connection() = null
            override fun call() = throw UnsupportedOperationException()
            override fun connectTimeoutMillis() = 0
            override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
            override fun readTimeoutMillis() = 0
            override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
            override fun writeTimeoutMillis() = 0
            override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        }

        interceptor.intercept(chain)
        passedRequest?.url?.queryParameter("tid") shouldBe "3"
    }
})
