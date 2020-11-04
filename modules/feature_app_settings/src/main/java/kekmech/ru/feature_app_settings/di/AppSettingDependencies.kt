package kekmech.ru.feature_app_settings.di

import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsFeatureFactory

internal data class AppSettingDependencies(
    val appSettingsFeatureFactory: AppSettingsFeatureFactory,
    val onboardingFeatureLauncher: OnboardingFeatureLauncher,
    val favoritesFeatureFactory: FavoritesFeatureFactory
)