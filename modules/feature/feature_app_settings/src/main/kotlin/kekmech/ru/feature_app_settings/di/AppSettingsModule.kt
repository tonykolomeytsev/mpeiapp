package kekmech.ru.feature_app_settings.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_app_settings.launcher.AppSettingsFeatureLauncherImpl
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesActor
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsActor
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsFeatureFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind

private const val MPEIX_PREFERENCES_NAME = "mpeix"

object AppSettingsModule : ModuleProvider({
    factory {
        androidContext().getSharedPreferences(MPEIX_PREFERENCES_NAME, Context.MODE_PRIVATE)
    } bind SharedPreferences::class
    factory { AppSettingsRepository(get()) }
    factory { AppSettingsActor(get(), get(), get()) } bind AppSettingsActor::class
    factory { AppSettingsFeatureFactory(get(), get()) }
    factory { AppSettingDependencies(get(), get(), get(), get()) }
    factory { AppSettingsFeatureLauncherImpl(get()) } bind AppSettingsFeatureLauncher::class
    factory { FavoritesActor(get()) }
    factory { FavoritesFeatureFactory(get()) }
})
