package kekmech.ru.feature_app_settings_api.data

import kekmech.ru.feature_app_settings_api.domain.AppSettings

interface AppSettingsRepository {

    fun getAppSettings(): AppSettings

    fun updateAppSettings(updateAction: AppSettings.() -> AppSettings): AppSettings
}
