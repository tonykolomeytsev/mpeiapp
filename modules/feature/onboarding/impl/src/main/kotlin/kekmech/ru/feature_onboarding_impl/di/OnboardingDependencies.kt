package kekmech.ru.feature_onboarding_impl.di

import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi

internal data class OnboardingDependencies(
    val scheduleFeatureApi: ScheduleFeatureApi,
    val appSettingsRepository: AppSettingsRepository,
    val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
)
