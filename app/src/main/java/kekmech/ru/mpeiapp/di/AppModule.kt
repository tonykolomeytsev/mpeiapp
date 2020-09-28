package kekmech.ru.mpeiapp.di

import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.mpeiapp.Prefetcher
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import java.util.*

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
    single { Prefetcher(get()) } bind Prefetcher::class
    single { FirebaseAnalytics.getInstance(androidApplication()) } bind FirebaseAnalytics::class
})
