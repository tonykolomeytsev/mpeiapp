package kekmech.ru.feature_app_settings_api

interface AppSettingsFeatureLauncher {

    fun launch(subPage: SubPage? = null)

    enum class SubPage { FAVORITES }
}
