package kekmech.ru.feature_dashboard_impl.di

import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_search_api.SearchFeatureLauncher
import kekmech.ru.library_navigation.BottomTabsSwitcher

internal data class DashboardDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val searchFeatureLauncher: SearchFeatureLauncher,
    val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
)
