package kekmech.ru.feature_app_settings.di

import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsFeatureFactory

internal data class AppSettingDependencies(
    val appSettingsFeatureFactory: AppSettingsFeatureFactory,
    val onboardingFeatureLauncher: OnboardingFeatureLauncher,
    val favoritesFeatureFactory: FavoritesFeatureFactory,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher
)