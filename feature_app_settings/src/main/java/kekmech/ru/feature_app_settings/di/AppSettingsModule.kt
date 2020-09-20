package kekmech.ru.feature_app_settings.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.feature_app_settings.presentation.AppSettingsActor
import kekmech.ru.feature_app_settings.presentation.AppSettingsFeatureFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind

private const val MPEIX_PREFERENCES_NAME = "mpeix"

object AppSettingsModule : ModuleProvider({
    factory {
        androidContext().getSharedPreferences(MPEIX_PREFERENCES_NAME, Context.MODE_PRIVATE)
    } bind SharedPreferences::class
    factory { AppSettingsRepository(get()) } bind AppSettingsRepository::class
    factory { AppSettingsActor(get()) } bind AppSettingsActor::class
    factory { AppSettingsFeatureFactory(get()) } bind AppSettingsFeatureFactory::class
    factory { AppSettingDependencies(get()) } bind AppSettingDependencies::class
})