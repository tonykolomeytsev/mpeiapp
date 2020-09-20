package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.Completable
import kekmech.ru.common_shared_preferences.boolean

class AppSettingsRepository(
    preferences: SharedPreferences
) : AppSettings {

    override var changeDayAfterChangeWeek by preferences.boolean("schedule_change_day_after_change_week", false)

    override var autoScrollToTheNextWeek by preferences.boolean("schedule_auto_scroll_to_the_next_week", false)

    fun complete(runnable: AppSettingsRepository.() -> Unit): Completable =
        Completable.fromRunnable { runnable() }
}