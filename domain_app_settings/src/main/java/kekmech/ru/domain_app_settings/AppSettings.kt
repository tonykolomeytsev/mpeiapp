package kekmech.ru.domain_app_settings

interface AppSettings {
    // all app
    val isDarkThemeEnabled: Boolean

    // schedule
    val changeDayAfterChangeWeek: Boolean
}