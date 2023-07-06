package kekmech.ru.feature_dashboard_impl.di

import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher
import kekmech.ru.feature_search_api.SearchFeatureLauncher
import kekmech.ru.lib_navigation.BottomTabsSwitcher

internal data class DashboardDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val searchFeatureLauncher: SearchFeatureLauncher,
    val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
)
