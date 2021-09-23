package kekmech.ru.feature_app_settings.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_app_settings.GitHubService
import kekmech.ru.feature_app_settings.launcher.AppSettingsFeatureLauncherImpl
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesActor
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsActor
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsFeatureFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import retrofit2.Retrofit

private const val MPEIX_PREFERENCES_NAME = "mpeix"

object AppSettingsModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<GitHubService>() } bind GitHubService::class
    factory {
        androidContext().getSharedPreferences(MPEIX_PREFERENCES_NAME, Context.MODE_PRIVATE)
    } bind SharedPreferences::class
    factory { AppSettingsRepository(get(), get(), get()) } binds arrayOf(AppSettingsRepository::class, AppSettings::class)
    factory { AppSettingsActor(get(), get()) } bind AppSettingsActor::class
    factory { AppSettingsFeatureFactory(get(), get()) }
    factory { AppSettingDependencies(get(), get(), get(), get()) }
    factory { AppSettingsFeatureLauncherImpl(get()) } bind AppSettingsFeatureLauncher::class
    factory { FavoritesActor(get()) }
    factory { FavoritesFeatureFactory(get()) }
})