package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings.State
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_ENABLED
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.intervalHours
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.isEnabled
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenPrefs
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenPrefs.isLockScreenEnabled
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenScheduleController
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderController
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.areRemindersEnabled
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Switches of the main settings screen. They work on the same preferences and schedulers as the
 * detailed notification screen, so both screens always show the same state.
 */
internal class ScheduleNotificationSettingsImpl(
    context: Context,
    private val classReminders: ClassReminderController,
    private val changeScheduler: ScheduleChangeScheduler,
    private val lockScreen: LockScreenScheduleController,
) : ScheduleNotificationSettings {

    private val changePrefs = ScheduleNotificationPrefs.of(context)
    private val reminderPrefs = ClassReminderPrefs.of(context)
    private val lockScreenPrefs = LockScreenPrefs.of(context)

    override fun current(): State =
        State(
            classReminders = reminderPrefs.areRemindersEnabled(),
            scheduleChanges = changePrefs.isEnabled(),
            lockScreenSchedule = lockScreenPrefs.isLockScreenEnabled(),
        )

    override fun observe(): Flow<State> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> trySend(current()) }
        changePrefs.registerOnSharedPreferenceChangeListener(listener)
        reminderPrefs.registerOnSharedPreferenceChangeListener(listener)
        lockScreenPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(current())
        awaitClose {
            changePrefs.unregisterOnSharedPreferenceChangeListener(listener)
            reminderPrefs.unregisterOnSharedPreferenceChangeListener(listener)
            lockScreenPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override fun setClassRemindersEnabled(enabled: Boolean) {
        classReminders.setEnabled(enabled)
    }

    override fun setScheduleChangesEnabled(enabled: Boolean) {
        changePrefs.edit { putBoolean(KEY_ENABLED, enabled) }
        if (enabled) {
            changeScheduler.startPeriodicChecks(changePrefs.intervalHours())
        } else {
            changeScheduler.stopPeriodicChecks()
        }
    }

    override fun setLockScreenScheduleEnabled(enabled: Boolean) {
        lockScreen.setEnabled(enabled)
    }
}
