package kekmech.ru.feature_app_settings_impl.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.feature_app_settings_api.data.AppEnvironmentRepository
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_impl.data.AppEnvironmentRepositoryImpl
import kekmech.ru.feature_app_settings_impl.data.AppSettingsRepositoryImpl
import kekmech.ru.feature_app_settings_impl.launcher.AppSettingsFeatureLauncherImpl
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesActor
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsActor
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.library_feature_toggles.RemoteVariable
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val MpeixPreferencesName = "mpeix"

val FeatureAppSettingsModule = module {
    factory {
        androidContext().getSharedPreferences(MpeixPreferencesName, Context.MODE_PRIVATE)
    } bind SharedPreferences::class
    factoryOf(::AppSettingsRepositoryImpl) bind AppSettingsRepository::class
    factoryOf(::AppEnvironmentRepositoryImpl) bind AppEnvironmentRepository::class

    factoryOf(::AppSettingsActor) bind AppSettingsActor::class
    factoryOf(::AppSettingsStoreFactory)
    factoryOf(::AppSettingDependencies)
    factoryOf(::AppSettingsFeatureLauncherImpl) bind AppSettingsFeatureLauncher::class

    factoryOf(::FavoritesActor)
    factoryOf(::FavoritesStoreFactory)

    factoryOf(::IsSnowFlakesEnabledFeatureToggle) bindIntoList RemoteVariable::class
}
