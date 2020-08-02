package kekmech.ru.mpeiapp.di

import kekmech.ru.common_di.ModuleProvider
import org.koin.dsl.bind
import java.util.*

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
})
