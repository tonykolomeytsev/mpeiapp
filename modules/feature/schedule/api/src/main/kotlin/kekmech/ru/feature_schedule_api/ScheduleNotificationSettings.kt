package kekmech.ru.feature_schedule_api

import kotlinx.coroutines.flow.Flow

/**
 * On / off switches of the schedule notifications, for the main settings screen.
 * The detailed screen (intervals, statistics) is [ScheduleFeatureApi.getNotificationSettingsScreen].
 */
public interface ScheduleNotificationSettings {

    public data class State(
        /** Reminders 24 h and 5 h before practices and laboratory works */
        val classReminders: Boolean,
        /** "The schedule has changed" notifications */
        val scheduleChanges: Boolean,
        /** Persistent notification with the current and the next class, also on the lock screen */
        val lockScreenSchedule: Boolean = false,
    )

    public fun current(): State

    /** Emits the current state and every change of it, also those made on the detailed screen */
    public fun observe(): Flow<State>

    public fun setClassRemindersEnabled(enabled: Boolean)

    public fun setScheduleChangesEnabled(enabled: Boolean)

    public fun setLockScreenScheduleEnabled(enabled: Boolean)
}
