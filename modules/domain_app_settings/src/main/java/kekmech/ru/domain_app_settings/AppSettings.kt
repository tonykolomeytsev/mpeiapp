package kekmech.ru.domain_app_settings

interface AppSettings {
    // all app
    val isDarkThemeEnabled: Boolean
    val isSnowEnabled: Boolean
    val languageCode: String?

    // schedule
    val changeDayAfterChangeWeek: Boolean

    // map
    val autoHideBottomSheet: Boolean

    // debug
    val isDebugEnvironment: Boolean
}