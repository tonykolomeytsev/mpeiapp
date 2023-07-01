package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_app_settings.feature_toggle.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_dashboard.DashboardFeatureLauncher
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher

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
