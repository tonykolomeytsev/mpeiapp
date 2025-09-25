package kekmech.ru.feature_app_settings_impl.data

import android.content.SharedPreferences
import kekmech.ru.ext_shared_preferences.boolean
import kekmech.ru.ext_shared_preferences.string
import kekmech.ru.ext_shared_preferences.typed
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

internal class AppSettingsRepositoryImpl(
    preferences: SharedPreferences,
) : AppSettingsRepository {

    // TODO: Удалить во время каникул между семестрами и сделать deafultValue = AppTheme.System
    private var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)
    private var appTheme by preferences.typed(
        key = "app_theme",
        toString = AppTheme::name,
        fromString = AppTheme::valueOf,
        defaultValue = when (isDarkThemeEnabled) {
            true -> AppTheme.Dark
            else -> AppTheme.System
        },
    )
    private var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)
    private var showNavigationButton by preferences.boolean("show_nav_fab", true)
    private var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)
    private var languageCode: String by preferences.string(
        key = "app_lang",
        defaultValue = when (Locale.getDefault().language) {
            "ru" -> "ru_RU"
            else -> "en_US"
        }
    )
    private var mapAppearanceType: String by preferences.string("app_map_type", "scheme")

    private val appThemeStateFlow = MutableStateFlow(appTheme)

    override fun getAppSettings(): AppSettings =
        AppSettings(
            isDarkThemeEnabled = isDarkThemeEnabled,
            appTheme = appTheme,
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
                appTheme = it.appTheme
                appThemeStateFlow.value = it.appTheme
                isSnowEnabled = it.isSnowEnabled
                showNavigationButton = it.showNavigationButton
                autoHideBottomSheet = it.autoHideBottomSheet
                languageCode = it.languageCode
                mapAppearanceType = it.mapAppearanceType
            }

    fun observeAppTheme(): StateFlow<AppTheme> = appThemeStateFlow
}
