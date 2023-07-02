package kekmech.ru.ext_okhttp

import okhttp3.Interceptor
import okhttp3.Response

class RequiredHeadersInterceptor(
    private val deviceId: String,
    private val appVersion: String,
    private val deviceLanguage: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request().newBuilder()
            .header("User-Agent", "mpeixAndroidClient/1.0")
            .header("X-Device-Id", deviceId)
            .header("X-Device-Os", "Android")
            .header("X-Device-Locale", deviceLanguage)
            .header("X-App-Version", appVersion)
            .build()
            .let(chain::proceed)
}
