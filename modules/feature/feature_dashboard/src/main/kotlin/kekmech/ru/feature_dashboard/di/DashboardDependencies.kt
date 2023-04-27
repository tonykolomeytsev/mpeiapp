package kekmech.ru.feature_dashboard.di

import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardFeatureFactory

internal data class DashboardDependencies(
    val dashboardFeatureFactory: DashboardFeatureFactory,
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val searchFeatureLauncher: SearchFeatureLauncher,
    val appSettingsFeatureLauncher: AppSettingsFeatureLauncher
)
