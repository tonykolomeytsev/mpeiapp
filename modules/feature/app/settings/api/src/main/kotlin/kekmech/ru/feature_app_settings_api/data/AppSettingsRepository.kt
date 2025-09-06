package kekmech.ru.feature_app_settings_api.data

import kekmech.ru.feature_app_settings_api.domain.model.AppSettings

public interface AppSettingsRepository {

    public fun getAppSettings(): AppSettings

    public fun updateAppSettings(updateAction: AppSettings.() -> AppSettings): AppSettings
}
