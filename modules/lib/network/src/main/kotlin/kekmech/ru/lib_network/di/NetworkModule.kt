package kekmech.ru.lib_network.di

import kekmech.ru.ext_json.LocalDateSerializer
import kekmech.ru.ext_json.LocalDateTimeSerializer
import kekmech.ru.ext_json.LocalTimeSerializer
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.ext_okhttp.NoConnectionInterceptor
import kekmech.ru.ext_okhttp.RequiredHeadersInterceptor
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_network.device_id.DeviceIdProvider
import kekmech.ru.lib_network.device_id.DeviceLocaleProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

private const val DEFAULT_REQUEST_TIMEOUT = 15L

public val LibraryNetworkModule: Module = module {
    factoryOf(::DeviceIdProvider)
    factoryOf(::DeviceLocaleProvider)
    singleOf(::provideRetrofitBuilder) bind Retrofit.Builder::class
    singleOf(::provideJson) bind Json::class
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

private fun provideJson() = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    coerceInputValues = true
    serializersModule = SerializersModule {
        contextual(LocalDate::class, LocalDateSerializer)
        contextual(LocalTime::class, LocalTimeSerializer)
        contextual(LocalDateTime::class, LocalDateTimeSerializer)
    }
}

private fun provideOkHttpClient(interceptors: List<Interceptor>) = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
    .apply { interceptors() += interceptors }
    .readTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .connectTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
    .build()

private fun provideRetrofitBuilder(
    okHttpClient: OkHttpClient,
    json: Json,
) = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
