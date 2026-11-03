package kekmech.ru.feature_app_settings_api

public interface AppSettingsFeatureLauncher {

    public fun launch(subPage: SubPage? = null)

    /**
     * Open the language selection dialog.
     * The selected language code (e.g. `ru_RU`) is returned by [resultKey],
     * only if it differs from the current one. Saving it is up to the caller.
     */
    public fun launchLanguageSelection(resultKey: String)

    public enum class SubPage { FAVORITES }
}
