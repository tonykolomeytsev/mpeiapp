package kekmech.ru.feature_app_settings_api.domain

data class AppSettings(
    // all app
    @Deprecated("use appTheme")
    val isDarkThemeEnabled: Boolean,
    val appTheme: AppTheme,
    val isSnowEnabled: Boolean,
    val languageCode: String,

    // schedule
    val showNavigationButton: Boolean,

    // map
    val autoHideBottomSheet: Boolean,
    val mapAppearanceType: String,
)
