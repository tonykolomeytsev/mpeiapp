package kekmech.ru.domain_app_settings

public interface AppSettingsFeatureLauncher {
    public fun launch(subPage: SubPage? = null)

    public enum class SubPage { FAVORITES }
}
