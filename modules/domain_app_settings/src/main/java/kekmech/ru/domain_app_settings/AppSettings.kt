package kekmech.ru.domain_app_settings

interface AppSettings {
    // all app
    val isDarkThemeEnabled: Boolean
    val isSnowEnabled: Boolean

    // schedule
    val changeDayAfterChangeWeek: Boolean

    // map
    val autoHideBottomSheet: Boolean
}