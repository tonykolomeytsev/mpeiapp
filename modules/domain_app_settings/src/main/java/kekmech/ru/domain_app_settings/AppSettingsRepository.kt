package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.Completable
import kekmech.ru.common_shared_preferences.boolean

class AppSettingsRepository(
    preferences: SharedPreferences
) : AppSettings {

    override var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)

    override var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)

    override var changeDayAfterChangeWeek by preferences.boolean("schedule_change_day_after_change_week", false)

    override var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)

    override var isDebugEnvironment by preferences.boolean("is_debug_env", false)

    fun complete(runnable: AppSettingsRepository.() -> Unit): Completable =
        Completable.fromRunnable { runnable() }
}