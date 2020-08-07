package kekmech.ru.common_network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.device_id.DeviceIdProvider
import kekmech.ru.common_network.gson.*
import kekmech.ru.common_network.okhttp.RequiredHeadersInterceptor
import kekmech.ru.common_network.retrofit.buildApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.bind
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object NetworkModule : ModuleProvider({
    single { provideGson() } bind Gson::class
    single { RequiredHeadersInterceptor(deviceId = provideDeviceId(get()), appVersion = "1.0.0") }
    single { provideOkHttpClient(get()) } bind OkHttpClient::class
    single { provideRetrofitBuilder(get(), get()) } bind Retrofit.Builder::class
    single { DeviceIdProvider(get()) } bind DeviceIdProvider::class
})

private fun provideGson() = GsonBuilder()
    .setDateFormat(DateFormat.LONG)
    .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
    .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
    .create()

private fun provideOkHttpClient(
    requiredHeadersInterceptor: RequiredHeadersInterceptor
) = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
    .addInterceptor(requiredHeadersInterceptor)
    .readTimeout(15, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .connectTimeout(15, TimeUnit.SECONDS)
    .build()

private fun provideRetrofitBuilder(
    okHttpClient: OkHttpClient,
    gson: Gson
) = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

private fun provideDeviceId(deviceIdProvider: DeviceIdProvider) = deviceIdProvider.getDeviceId()