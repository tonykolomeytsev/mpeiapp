package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import kekmech.ru.common_shared_preferences.boolean
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_app_settings_models.AppSettings

class AppSettingsRepository(
    preferences: SharedPreferences,
) {

    private var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)
    private var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)
    private var showNavigationButton by preferences.boolean("show_nav_fab", true)
    private var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)
    private var languageCode: String by preferences.string("app_lang", "ru_RU")
    private var mapAppearanceType: String by preferences.string("app_map_type", "hybrid")

    fun getAppSettings(): AppSettings =
        AppSettings(
            isDarkThemeEnabled = isDarkThemeEnabled,
            isSnowEnabled = isSnowEnabled,
            languageCode = languageCode,
            showNavigationButton = showNavigationButton,
            autoHideBottomSheet = autoHideBottomSheet,
            mapAppearanceType = mapAppearanceType,
        )

    fun changeAppSettings(transform: AppSettings.() -> AppSettings): AppSettings =
        getAppSettings()
            .let(transform)
            .also {
                isDarkThemeEnabled = it.isDarkThemeEnabled
                isSnowEnabled = it.isSnowEnabled
                showNavigationButton = it.showNavigationButton
                autoHideBottomSheet = it.autoHideBottomSheet
                languageCode = it.languageCode
                mapAppearanceType = it.mapAppearanceType
            }
}
