package kekmech.ru.feature_app_settings_api.domain.model

public data class AppSettings(
    // all app
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
