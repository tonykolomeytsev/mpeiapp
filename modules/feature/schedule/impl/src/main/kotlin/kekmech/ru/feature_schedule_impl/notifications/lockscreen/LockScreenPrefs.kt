package kekmech.ru.feature_schedule_impl.notifications.lockscreen

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/** Switch of the lock screen schedule notification. Off by default: it is a persistent notification. */
internal object LockScreenPrefs {

    private const val PREFS_NAME = "lock_screen_schedule"
    private const val KEY_ENABLED = "enabled"

    fun of(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun SharedPreferences.isLockScreenEnabled(): Boolean = getBoolean(KEY_ENABLED, false)

    fun SharedPreferences.setLockScreenEnabled(enabled: Boolean) = edit { putBoolean(KEY_ENABLED, enabled) }
}
