package kekmech.ru.feature_app_settings_impl.data

import android.content.SharedPreferences
import kekmech.ru.ext_shared_preferences.boolean
import kekmech.ru.ext_shared_preferences.string
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_api.domain.AppSettings

internal class AppSettingsRepositoryImpl(
    preferences: SharedPreferences,
) : AppSettingsRepository {

    private var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)
    private var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)
    private var showNavigationButton by preferences.boolean("show_nav_fab", true)
    private var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)
    private var languageCode: String by preferences.string("app_lang", "ru_RU")
    private var mapAppearanceType: String by preferences.string("app_map_type", "hybrid")

    override fun getAppSettings(): AppSettings =
        AppSettings(
            isDarkThemeEnabled = isDarkThemeEnabled,
            isSnowEnabled = isSnowEnabled,
            languageCode = languageCode,
            showNavigationButton = showNavigationButton,
            autoHideBottomSheet = autoHideBottomSheet,
            mapAppearanceType = mapAppearanceType,
        )

    override fun updateAppSettings(updateAction: AppSettings.() -> AppSettings): AppSettings =
        getAppSettings()
            .let(updateAction)
            .also {
                isDarkThemeEnabled = it.isDarkThemeEnabled
                isSnowEnabled = it.isSnowEnabled
                showNavigationButton = it.showNavigationButton
                autoHideBottomSheet = it.autoHideBottomSheet
                languageCode = it.languageCode
                mapAppearanceType = it.mapAppearanceType
            }
}
