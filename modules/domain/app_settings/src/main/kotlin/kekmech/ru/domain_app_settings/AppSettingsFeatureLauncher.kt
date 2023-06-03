package kekmech.ru.domain_app_settings

interface AppSettingsFeatureLauncher {
    fun launch(subPage: SubPage? = null)

    enum class SubPage { FAVORITES }
}
