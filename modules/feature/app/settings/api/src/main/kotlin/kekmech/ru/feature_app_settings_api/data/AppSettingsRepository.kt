package kekmech.ru.feature_app_settings_api.data

import kekmech.ru.feature_app_settings_api.domain.AppSettings
import kekmech.ru.feature_app_settings_api.domain.AppTheme
import kotlinx.coroutines.flow.StateFlow

interface AppSettingsRepository {

    fun getAppSettings(): AppSettings

    fun updateAppSettings(updateAction: AppSettings.() -> AppSettings): AppSettings

    fun observeAppTheme(): StateFlow<AppTheme>
}
