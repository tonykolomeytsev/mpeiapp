package kekmech.ru.repository.gateways

import android.util.Log
import kekmech.ru.core.exceptions.NotLoggedInBarsException
import kekmech.ru.core.exceptions.StudentPageNotFoundBarsException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.internal.http.RealResponseBody
import okio.GzipSource
import okio.buffer
import java.net.CookieManager
import java.net.URI
import java.util.concurrent.TimeUnit

class BarsHttpClient(
    private val debug: Boolean = false,
    private val defaultUserAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36",
    private val hiddenFieldsExtractor: (String) -> Map<String, String> = { emptyMap() },
    private val studentPageLocationExtractor: (String) -> String = { "" },
    private val semesterListExtractor: (String) -> Map<String, String> = { emptyMap() }
) {
    private val cookies = mutableListOf<Cookie>()
    private val cookieManager = android.webkit.CookieManager.getInstance()
    private val cookieStore = CookieStore(cookies, cookieManager)
    private val webkitCookies get() = android.webkit.CookieManager.getInstance().getCookie("bars.mpei.ru")
        .split(";")
        .mapNotNull { Cookie.parse("https://bars.mpei.ru/".toHttpUrlOrNull()!!, it.trim()) }

    private val okHttp = OkHttpClient.Builder()
        .callTimeout(15, TimeUnit.SECONDS)
        .connectionSpecs(arrayListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
        .followRedirects(true)
        .followSslRedirects(true)
        .addInterceptor(LoggingInterceptor())
        .addInterceptor(UnzippingInterceptor())
        .cookieJar(cookieStore) //JavaNetCookieJar(CookieManager().apply { setCookiePolicy(ACCEPT_ALL) })
        .build()
    private val baseHeaders = arrayListOf(
        "Host", "bars.mpei.ru",
        "Origin", BARS_MAIN_DOMAIN,
        "Connection", "keep-alive",
        "Pragma", "no-cache",
        "Cache-Control", "no-cache",
        "Upgrade-Insecure-Requests", "1",
        "User-Agent", defaultUserAgent,
        "Sec-Fetch-User", "?1",
        "Referer", "$BARS_MAIN_DOMAIN/bars_web",
        "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Sec-Fetch-Site", "same-origin",
        "Sec-Fetch-Mode", "navigate",
        "Accept-Encoding", "gzip, br",
        "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"
    ).toTypedArray()
    private val barsMainUrl: String = "$BARS_MAIN_DOMAIN/BARS_Web"
    private val studentPart1Url: String = "$BARS_MAIN_DOMAIN/bars_web/Student/Part1"
    private val studentPageLocation: String
        get() = "$studentPart1Url?studentID=$studentId"
    private val semesterIds = mutableMapOf<String, String>()

    var studentId: String = ""
    val isLoggedIn: Boolean
        get() = cookies.any { it.name == COOKIE_NAME_AUTH_BARS } || webkitCookies.any {  it.name == COOKIE_NAME_AUTH_BARS }

    @Throws(StudentPageNotFoundBarsException::class, NotLoggedInBarsException::class)
    fun login() {
        // TODO сделать получение валидного браузерного токена
        val mainPageResponse = Request.Builder()
            .url(barsMainUrl)
            .headers(Headers.headersOf(*baseHeaders))
            .header("Sec-Fetch-Site", "cross-site")
            .header("Referer", "https://www.google.com/")
            .removeHeader("Origin")
            .get()
            .build()
            .let { okHttp.newCall(it) }
            .execute()

        val formBody = FormBody.Builder().apply {
            hiddenFieldsExtractor(mainPageResponse.body?.string() ?: "").forEach { (k, v) -> add(k, v) }
        }.build()
        val loginResponse = Request.Builder()
            .url(barsMainUrl)
            .headers(Headers.headersOf(*baseHeaders))
            .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
            .header("Content-Length", (10 + formBody.contentLength()).toString())
            .post(formBody)
            .build()
            .let { okHttp.newCall(it) }
            .execute()
        if (!cookies.any { it.name == "auth_bars" }) throw NotLoggedInBarsException("Does not have a 'bars_auth' cookie")

        val location = loginResponse.headers["Location"] ?: ""
        if (location.matches(".*Student/Part1.*".toRegex())) {
            studentId = location.getStudentId()
        } else {
            val listPageResponse = Request.Builder()
                .url("https://bars.mpei.ru/bars_web/Student/ListStudent")
                .headers(Headers.headersOf(*baseHeaders))
                .get()
                .build()
                .let { okHttp.newCall(it) }
                .execute()
            studentId = studentPageLocationExtractor(listPageResponse.body?.string() ?: "").getStudentId()
        }

        if (studentId.isBlank()) throw StudentPageNotFoundBarsException("Can't found student's list page :(")
    }

    @Throws(StudentPageNotFoundBarsException::class)
    fun loginWithWebkitCookies() {
        val loginResponse = Request.Builder()
            .url(barsMainUrl)
            .headers(Headers.headersOf(*baseHeaders))
            .get()
            .build()
            .let { okHttp.newCall(it) }
            .execute()

        val location = loginResponse.headers["Location"] ?: ""
        if (location.matches(".*Student/Part1.*".toRegex())) {
            studentId = location.getStudentId()
        } else {
            val listPageResponse = Request.Builder()
                .url("https://bars.mpei.ru/bars_web/Student/ListStudent")
                .headers(Headers.headersOf(*baseHeaders))
                .get()
                .build()
                .let { okHttp.newCall(it) }
                .execute()
            studentId = studentPageLocationExtractor(listPageResponse.body?.string() ?: "").getStudentId()
        }

        if (studentId.isBlank()) throw StudentPageNotFoundBarsException("Can't found student's list page :(")
    }

    @Throws(StudentPageNotFoundBarsException::class)
    fun loadSemesterPage(): String {
        if (studentId.isBlank()) throw StudentPageNotFoundBarsException("Can't found student's list page :(")

        val studentPageResponse = Request.Builder()
            .url(studentPageLocation)
            .headers(Headers.headersOf(*baseHeaders))
            .get()
            .build()
            .let { okHttp.newCall(it) }
            .execute()

        val body = studentPageResponse.body?.string() ?: ""
        semesterIds.clear()
        semesterIds.putAll(semesterListExtractor(body))
        return body
    }

    internal class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            println(">> ${request.method} REQUEST ${request.url} \n${request.headers} \n${request.body}")
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            println("<< RESPONSE for ${response.request.url} in ${(t2 - t1) / 1e6}ms \n${response.headers}")
            return response
        }
    }

    internal class UnzippingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain) : Response {
            val response = chain.proceed(chain.request());
            return unzip(response)
        }

        // copied from okhttp3.internal.http.HttpEngine (because is private)
        private fun unzip(response: Response): Response {
            if (response.body == null) return response

            //check if we have gzip response
            val contentEncoding = response.headers["Content-Encoding"]

            //this is used to decompress gzipped responses
            return if (contentEncoding != null && contentEncoding == "gzip") {
                val contentLength = response.body!!.contentLength();
                val responseBody = GzipSource(response.body!!.source());
                val strippedHeaders = response.headers.newBuilder().build();
                response.newBuilder().headers(strippedHeaders)
                    .body(RealResponseBody(response.body!!.contentType().toString(), contentLength, responseBody.buffer()))
                    .build()
            } else {
                response
            }
        }
    }

    inner class CookieStore(
        private val store: MutableList<Cookie>,
        private val webkitCookieManager: android.webkit.CookieManager
    ) : CookieJar {

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            val response = mutableListOf<Cookie>()
            response.addAll(store)
            response.addAll(webkitCookies)

            return response
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            store.addAll(cookies)
        }

    }

    companion object {
        const val COOKIE_NAME_AUTH_BARS = "auth_bars"
        const val BARS_MAIN_DOMAIN = "https://bars.mpei.ru"
    }

    private fun String.getStudentId(): String = ".*student..=(.*).*".toRegex().find(this)?.groups?.get(1)?.value ?: ""
}