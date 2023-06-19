package kekmech.ru.feature_app_settings.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_app_settings.launcher.AppSettingsFeatureLauncherImpl
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesActor
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesStoreFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsActor
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsStoreFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val MpeixPreferencesName = "mpeix"

val FeatureAppSettingsModule = module {
    factory {
        androidContext().getSharedPreferences(MpeixPreferencesName, Context.MODE_PRIVATE)
    } bind SharedPreferences::class
    factoryOf(::AppSettingsRepository)
    factoryOf(::AppSettingsActor) bind AppSettingsActor::class
    factoryOf(::AppSettingsStoreFactory)
    factoryOf(::AppSettingDependencies)
    factoryOf(::AppSettingsFeatureLauncherImpl) bind AppSettingsFeatureLauncher::class
    factoryOf(::FavoritesActor)
    factoryOf(::FavoritesStoreFactory)
}
