package kekmech.ru.lib_network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kekmech.ru.ext_gson.LocalDateJsonAdapter
import kekmech.ru.ext_gson.LocalDateTimeJsonAdapter
import kekmech.ru.ext_gson.LocalTimeJsonAdapter
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.ext_okhttp.NoConnectionInterceptor
import kekmech.ru.ext_okhttp.RequiredHeadersInterceptor
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_network.device_id.DeviceIdProvider
import kekmech.ru.lib_network.device_id.DeviceLocaleProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

private const val DEFAULT_REQUEST_TIMEOUT = 15L

public val LibraryNetworkModule: Module = module {
    factoryOf(::DeviceIdProvider)
    factoryOf(::DeviceLocaleProvider)
    singleOf(::provideRetrofitBuilder) bind Retrofit.Builder::class
    singleOf(::provideGson) bind Gson::class
    single { provideOkHttpClient(getAll()) } bind OkHttpClient::class

    single {
        RequiredHeadersInterceptor(
            deviceId = get<DeviceIdProvider>().getDeviceId(),
            appVersion = get<AppVersionName>().versionName,
            deviceLanguage = get<DeviceLocaleProvider>().getLanguage(),
        )
    } bindIntoList Interceptor::class
    factoryOf(::NoConnectionInterceptor) bindIntoList Interceptor::class
}

private fun provideGson() = GsonBuilder()
    .setDateFormat(DateFormat.LONG)
    .registerTypeAdapter(LocalDate::class.java, LocalDateJsonAdapter())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeJsonAdapter())
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeJsonAdapter())
    .create()

private fun provideOkHttpClient(interceptors: List<Interceptor>) = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
    .apply { interceptors() += interceptors }
    .readTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .connectTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .build()

private fun provideRetrofitBuilder(
    okHttpClient: OkHttpClient,
    gson: Gson,
) = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
