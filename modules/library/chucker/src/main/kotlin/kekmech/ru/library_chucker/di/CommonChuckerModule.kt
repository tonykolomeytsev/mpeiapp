package kekmech.ru.library_chucker.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import kekmech.ru.ext_koin.bindIntoList
import okhttp3.Interceptor
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val LibraryChuckerModule = module {
    factoryOf(::createChuckerInterceptor) bindIntoList Interceptor::class
}

private fun createChuckerInterceptor(context: Context): Interceptor {
    // Create the Collector
    val chuckerCollector = ChuckerCollector(
        context = context,
        // Toggles visibility of the notification
        showNotification = false,
        // Allows to customize the retention period of collected data
        retentionPeriod = RetentionManager.Period.ONE_HOUR,
    )
    @Suppress("MagicNumber")
    return ChuckerInterceptor.Builder(context)
        .collector(chuckerCollector)
        // The max body content length in bytes, after this responses will be truncated.
        .maxContentLength(250_000L)
        // List of headers to replace with ** in the Chucker UI
        .redactHeaders("Auth-Token", "Bearer")
        // Read the whole response body even when the client does not consume the response.
        // This is useful in case of parsing errors or when the response body
        // is closed before being read like in Retrofit with Void and Unit types.
        .alwaysReadResponseBody(true)
        .build()
}
