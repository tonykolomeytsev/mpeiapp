package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_ENABLED
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_INTERVAL
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_LAST_CHECK_MILLIS
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_TOTAL_CHANGES
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.KEY_TOTAL_CHECKS
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.intervalHours
import kekmech.ru.feature_schedule_impl.notifications.ScheduleNotificationPrefs.isEnabled
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderController
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.areRemindersEnabled
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.isKindEnabled
import kekmech.ru.feature_schedule_impl.notifications.reminders.ReminderKind
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * State for notification settings
 */
internal data class ScheduleNotificationSettingsState(
    val notificationsEnabled: Boolean = false,
    val checkIntervalHours: Long = ScheduleChangeScheduler.DEFAULT_CHECK_INTERVAL_HOURS,
    val isCheckingNow: Boolean = false,
    val lastCheckTime: String? = null,
    val totalChecks: Int = 0,
    val totalChangesDetected: Int = 0,
    val classRemindersEnabled: Boolean = true,
    val remindDayBefore: Boolean = true,
    val remindHoursBefore: Boolean = true,
    /** System-level switch: POST_NOTIFICATIONS on Android 13+ and the per-app switch of older versions */
    val notificationsAllowed: Boolean = true,
)

/**
 * ViewModel for notification settings: persists the choice and drives [ScheduleChangeScheduler].
 * Statistics are written by [ScheduleChangeWorker] into the same preferences, so the
 * state is refreshed whenever they change.
 */
internal class ScheduleNotificationSettingsViewModel(
    private val context: Context,
    private val scheduler: ScheduleChangeScheduler,
    private val reminderController: ClassReminderController,
) {
    private val prefs = ScheduleNotificationPrefs.of(context)
    private val reminderPrefs = ClassReminderPrefs.of(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(readState(isCheckingNow = false))
    val state: StateFlow<ScheduleNotificationSettingsState> = _state

    // Must be held by a strong reference, SharedPreferences keeps listeners weakly
    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> refresh() }

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefsListener)
        reminderPrefs.registerOnSharedPreferenceChangeListener(prefsListener)
        scope.launch {
            scheduler.observeImmediateCheckRunning().collect { running ->
                _state.value = _state.value.copy(isCheckingNow = running)
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        Timber.d("Toggle notifications: $enabled")
        prefs.edit { putBoolean(KEY_ENABLED, enabled) }
        if (enabled) {
            scheduler.startPeriodicChecks(prefs.intervalHours())
        } else {
            scheduler.stopPeriodicChecks()
        }
    }

    fun setCheckInterval(hours: Long) {
        val interval = hours.coerceIn(
            ScheduleChangeScheduler.MIN_CHECK_INTERVAL_HOURS,
            ScheduleChangeScheduler.MAX_CHECK_INTERVAL_HOURS,
        )
        if (interval == prefs.intervalHours()) return
        Timber.d("Set check interval: $interval hours")
        prefs.edit { putLong(KEY_INTERVAL, interval) }
        if (prefs.isEnabled()) {
            scheduler.startPeriodicChecks(interval)
        }
    }

    fun toggleClassReminders(enabled: Boolean) {
        Timber.d("Toggle class reminders: $enabled")
        reminderController.setEnabled(enabled)
    }

    fun toggleReminderKind(kind: ReminderKind, enabled: Boolean) {
        Timber.d("Toggle class reminder $kind: $enabled")
        reminderController.setKindEnabled(kind, enabled)
    }

    /** The exact alarm permission is changed in the system settings, so the state is re-read on return */
    fun refresh() {
        _state.value = readState(isCheckingNow = _state.value.isCheckingNow)
    }

    fun checkNow() {
        if (_state.value.isCheckingNow) return
        Timber.d("Manual check triggered")
        scheduler.triggerImmediateCheck()
    }

    fun clear() {
        prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
        reminderPrefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
        scope.cancel()
    }

    private fun readState(isCheckingNow: Boolean): ScheduleNotificationSettingsState {
        val lastCheckMillis = prefs.getLong(KEY_LAST_CHECK_MILLIS, 0L)
        return ScheduleNotificationSettingsState(
            notificationsEnabled = prefs.isEnabled(),
            checkIntervalHours = prefs.intervalHours(),
            isCheckingNow = isCheckingNow,
            lastCheckTime = lastCheckMillis.takeIf { it > 0 }?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .format(TIME_FORMATTER)
            },
            totalChecks = prefs.getInt(KEY_TOTAL_CHECKS, 0),
            totalChangesDetected = prefs.getInt(KEY_TOTAL_CHANGES, 0),
            classRemindersEnabled = reminderPrefs.areRemindersEnabled(),
            remindDayBefore = reminderPrefs.isKindEnabled(ReminderKind.DAY_BEFORE),
            remindHoursBefore = reminderPrefs.isKindEnabled(ReminderKind.HOURS_BEFORE),
            notificationsAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled(),
        )
    }

    private companion object {
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM, HH:mm")
    }
}
