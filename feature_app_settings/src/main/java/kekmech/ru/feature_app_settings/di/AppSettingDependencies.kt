package kekmech.ru.feature_app_settings.di

import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_app_settings.presentation.AppSettingsFeatureFactory

internal data class AppSettingDependencies(
    val appSettingsFeatureFactory: AppSettingsFeatureFactory,
    val onboardingFeatureLauncher: OnboardingFeatureLauncher
)