package kekmech.ru.common_network.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kekmech.ru.common_di.AppVersionName
import kekmech.ru.common_network.device_id.DeviceIdProvider
import kekmech.ru.common_network.device_id.DeviceLocaleProvider
import kekmech.ru.common_network.gson.LocalDateJsonAdapter
import kekmech.ru.common_network.gson.LocalDateTimeJsonAdapter
import kekmech.ru.common_network.gson.LocalTimeJsonAdapter
import kekmech.ru.common_network.okhttp.NoConnectionInterceptor
import kekmech.ru.common_network.okhttp.RequiredHeadersInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

private const val DEFAULT_REQUEST_TIMEOUT = 15L

val CommonNetworkModule = module {
    factoryOf(::NoConnectionInterceptor)
    factoryOf(::DeviceIdProvider) bind DeviceIdProvider::class
    singleOf(::provideOkHttpClient) bind OkHttpClient::class
    singleOf(::provideRetrofitBuilder) bind Retrofit.Builder::class
    singleOf(::provideGson) bind Gson::class
    single {
        RequiredHeadersInterceptor(
            deviceId = provideDeviceId(get()),
            appVersion = get<AppVersionName>().versionName,
            deviceLanguage = provideDeviceLocale(get()),
        )
    }
}

private fun provideGson() = GsonBuilder()
    .setDateFormat(DateFormat.LONG)
    .registerTypeAdapter(LocalDate::class.java, LocalDateJsonAdapter())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeJsonAdapter())
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeJsonAdapter())
    .create()

private fun provideOkHttpClient(
    requiredHeadersInterceptor: RequiredHeadersInterceptor,
    noConnectionInterceptor: NoConnectionInterceptor,
) = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
    .addInterceptor(requiredHeadersInterceptor)
    .addInterceptor(noConnectionInterceptor)
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
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

private fun provideDeviceId(deviceIdProvider: DeviceIdProvider) = deviceIdProvider.getDeviceId()

private fun provideDeviceLocale(sharedPreferences: SharedPreferences) =
    DeviceLocaleProvider(sharedPreferences).getLanguage()
