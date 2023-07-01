package kekmech.ru.domain_app_settings_models

data class AppSettings(
    // all app
    val isDarkThemeEnabled: Boolean,
    val isSnowEnabled: Boolean,
    val languageCode: String,

    // schedule
    val showNavigationButton: Boolean,

    // map
    val autoHideBottomSheet: Boolean,
    val mapAppearanceType: String,
)
