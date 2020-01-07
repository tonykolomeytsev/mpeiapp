package kekmech.ru.settings.di

import kekmech.ru.settings.SettingsPresenter
import org.koin.dsl.module

val KoinSettingsModule = module {
    single { SettingsPresenter(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}