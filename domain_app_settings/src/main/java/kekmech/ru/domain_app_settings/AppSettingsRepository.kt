package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.Completable
import kekmech.ru.common_shared_preferences.boolean

class AppSettingsRepository(
    preferences: SharedPreferences
) : AppSettings {

    override var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", true)

    override var changeDayAfterChangeWeek by preferences.boolean("schedule_change_day_after_change_week", false)

    override var autoScrollToTheNextWeek by preferences.boolean("schedule_auto_scroll_to_the_next_week", false)

    fun complete(runnable: AppSettingsRepository.() -> Unit): Completable =
        Completable.fromRunnable { runnable() }
}