package kekmech.ru.feature_app_settings_impl.di

import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory

internal data class AppSettingDependencies(
    val appSettingsStoreFactory: AppSettingsStoreFactory,
    val onboardingFeatureLauncher: OnboardingFeatureLauncher,
    val favoritesStoreFactory: FavoritesStoreFactory,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
)