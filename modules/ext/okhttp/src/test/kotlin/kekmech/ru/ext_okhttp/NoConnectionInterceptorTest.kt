package kekmech.ru.ext_okhttp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.ConnectException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

internal class NoConnectionInterceptorTest {

    @Test
    fun `request goes out even when the device looks offline`() {
        // e.g. a system-wide VPN whose network doesn't report VALIDATED
        val response = execute(offline = true, downstream = respondOk)

        assertEquals(expected = 200, actual = response.code)
    }

    @Test
    fun `failure while offline becomes NoInternetConnectionException`() {
        val error = ConnectException("failed to connect")

        val thrown = assertFailsWith<NoInternetConnectionException> {
            execute(offline = true, downstream = fail(error))
        }
        assertSame(expected = error, actual = thrown.cause)
    }

    @Test
    fun `failure while online keeps the original error`() {
        val error = ConnectException("failed to connect")

        val thrown = assertFailsWith<ConnectException> {
            execute(offline = false, downstream = fail(error))
        }
        assertSame(expected = error, actual = thrown)
    }

    private fun execute(offline: Boolean, downstream: Interceptor): Response =
        OkHttpClient.Builder()
            .addInterceptor(NoConnectionInterceptor(isOffline = { offline }))
            // Answers or fails before OkHttp touches the network
            .addInterceptor(downstream)
            .build()
            .newCall(Request.Builder().url("https://example.invalid/").build())
            .execute()

    private val respondOk = Interceptor { chain ->
        Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("".toResponseBody())
            .build()
    }

    private fun fail(error: IOException) = Interceptor { throw error }
}
