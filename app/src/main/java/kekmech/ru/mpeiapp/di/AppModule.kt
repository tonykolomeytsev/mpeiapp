package kekmech.ru.mpeiapp.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.mpeiapp.Prefetcher
import org.koin.dsl.bind
import java.util.*

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
    single { Prefetcher(get()) } bind Prefetcher::class
})
