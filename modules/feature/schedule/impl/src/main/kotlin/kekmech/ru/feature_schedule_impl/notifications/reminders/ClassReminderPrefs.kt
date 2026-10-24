package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Settings of the practice / laboratory work reminders and the ids of the alarms
 * that are currently registered in the system (needed to cancel the outdated ones).
 * Reminders are on by default: nothing is shown until the notification permission is granted.
 */
internal object ClassReminderPrefs {

    private const val PREFS_NAME = "class_reminder_settings"
    private const val KEY_ENABLED = "enabled"
    private const val KEY_DAY_BEFORE = "remind_24h"
    private const val KEY_HOURS_BEFORE = "remind_5h"
    private const val KEY_SCHEDULED_IDS = "scheduled_ids"

    fun of(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun SharedPreferences.areRemindersEnabled(): Boolean = getBoolean(KEY_ENABLED, true)

    fun SharedPreferences.setRemindersEnabled(enabled: Boolean) = edit { putBoolean(KEY_ENABLED, enabled) }

    fun SharedPreferences.isKindEnabled(kind: ReminderKind): Boolean =
        getBoolean(kind.key(), true)

    fun SharedPreferences.setKindEnabled(kind: ReminderKind, enabled: Boolean) =
        edit { putBoolean(kind.key(), enabled) }

    /** Kinds that have to be scheduled right now (empty when the whole feature is off) */
    fun SharedPreferences.activeKinds(): Set<ReminderKind> =
        if (areRemindersEnabled()) ReminderKind.entries.filterTo(mutableSetOf()) { isKindEnabled(it) } else emptySet()

    fun SharedPreferences.scheduledIds(): Set<Int> =
        getStringSet(KEY_SCHEDULED_IDS, emptySet()).orEmpty().mapNotNullTo(mutableSetOf()) { it.toIntOrNull() }

    fun SharedPreferences.saveScheduledIds(ids: Set<Int>) =
        edit { putStringSet(KEY_SCHEDULED_IDS, ids.mapTo(mutableSetOf()) { it.toString() }) }

    private fun ReminderKind.key(): String =
        when (this) {
            ReminderKind.DAY_BEFORE -> KEY_DAY_BEFORE
            ReminderKind.HOURS_BEFORE -> KEY_HOURS_BEFORE
        }
}
