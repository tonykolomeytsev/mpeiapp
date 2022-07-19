package kekmech.ru.domain_app_settings

interface AppSettings {
    // all app
    val isDarkThemeEnabled: Boolean
    val isSnowEnabled: Boolean
    val languageCode: String

    // schedule
    val showNavigationButton: Boolean

    // map
    val autoHideBottomSheet: Boolean
    val mapAppearanceType: String

    // debug
    val isDebugEnvironment: Boolean
}