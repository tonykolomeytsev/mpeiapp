package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_dashboard.DashboardFeatureLauncher
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
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
