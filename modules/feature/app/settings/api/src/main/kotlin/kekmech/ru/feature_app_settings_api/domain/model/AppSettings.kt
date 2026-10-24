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

/**
 * Whether the dark palette has to be used for the theme chosen by the user.
 * [isSystemInDarkTheme] is the current system (night mode) setting, used for [AppTheme.System].
 */
public fun AppSettings.isDarkTheme(isSystemInDarkTheme: Boolean): Boolean =
    when (appTheme) {
        AppTheme.Light -> false
        AppTheme.Dark -> true
        AppTheme.System -> isSystemInDarkTheme
    }
