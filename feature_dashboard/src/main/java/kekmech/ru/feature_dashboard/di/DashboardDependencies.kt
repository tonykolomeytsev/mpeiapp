package kekmech.ru.feature_dashboard.di

import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_dashboard.presentation.DashboardFeatureFactory

data class DashboardDependencies(
    val dashboardFeatureFactory: DashboardFeatureFactory,
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher
)