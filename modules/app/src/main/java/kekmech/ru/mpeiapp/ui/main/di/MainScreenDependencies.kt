package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_update_api.ForceUpdateChecker
import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_dashboard_api.DashboardFeatureLauncher
import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.library_navigation.BottomTabsSwitcher

data class MainScreenDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val dashboardFeatureLauncher: DashboardFeatureLauncher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    val barsFeatureLauncher: BarsFeatureLauncher,
    val mapFeatureLauncher: MapFeatureLauncher,
    val forceUpdateChecker: ForceUpdateChecker,
    val isSnowFlakesEnabledFeatureToggle: IsSnowFlakesEnabledFeatureToggle,
    val appSettingsRepository: AppSettingsRepository,
)
