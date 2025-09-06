package kekmech.ru.feature_app_settings_api

public interface AppSettingsFeatureLauncher {

    public fun launch(subPage: SubPage? = null)

    public enum class SubPage { FAVORITES }
}
