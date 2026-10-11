package kekmech.ru.feature_schedule_impl.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * Settings screen for schedule change notifications
 */
@Composable
internal fun ScheduleNotificationSettingsScreen(
    viewModel: ScheduleNotificationSettingsViewModel
) {
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Enable/Disable notifications
        NotificationToggleCard(
            enabled = state.notificationsEnabled,
            onToggle = { viewModel.toggleNotifications(it) }
        )
        
        if (state.notificationsEnabled) {
            // Check interval setting
            CheckIntervalCard(
                intervalHours = state.checkIntervalHours,
                onIntervalChange = { viewModel.setCheckInterval(it) }
            )
            
            // Immediate check button
            ImmediateCheckCard(
                isChecking = state.isCheckingNow,
                lastCheckTime = state.lastCheckTime,
                onCheckNow = { viewModel.checkNow() }
            )
            
            // Statistics
            StatisticsCard(
                totalChecks = state.totalChecks,
                changesDetected = state.totalChangesDetected
            )
        }
    }
}

@Composable
private fun NotificationToggleCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Уведомления об изменениях",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Получать уведомления при изменении расписания",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Switch(
                checked = enabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@Composable
private fun CheckIntervalCard(
    intervalHours: Long,
    onIntervalChange: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Частота проверки",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "Проверять расписание каждые $intervalHours ч",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Slider(
                value = intervalHours.toFloat(),
                onValueChange = { onIntervalChange(it.toLong()) },
                valueRange = 1f..24f,
                steps = 23,
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1 час", style = MaterialTheme.typography.labelSmall)
                Text("24 часа", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun ImmediateCheckCard(
    isChecking: Boolean,
    lastCheckTime: String?,
    onCheckNow: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Проверить сейчас",
                style = MaterialTheme.typography.titleMedium
            )
            
            if (lastCheckTime != null) {
                Text(
                    text = "Последняя проверка: $lastCheckTime",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Button(
                onClick = onCheckNow,
                enabled = !isChecking,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isChecking) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Проверяем...")
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Проверить изменения")
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    totalChecks: Int,
    changesDetected: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Проверок",
                    value = totalChecks.toString()
                )
                
                StatItem(
                    icon = Icons.Default.Notifications,
                    label = "Изменений",
                    value = changesDetected.toString()
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * State for notification settings
 */
internal data class ScheduleNotificationSettingsState(
    val notificationsEnabled: Boolean = false,
    val checkIntervalHours: Long = 6L,
    val isCheckingNow: Boolean = false,
    val lastCheckTime: String? = null,
    val totalChecks: Int = 0,
    val totalChangesDetected: Int = 0
)

/**
 * ViewModel for notification settings: persists the choice and drives [ScheduleChangeScheduler]
 */
internal class ScheduleNotificationSettingsViewModel(
    context: Context,
    private val scheduler: ScheduleChangeScheduler,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(
        ScheduleNotificationSettingsState(
            notificationsEnabled = prefs.getBoolean(KEY_ENABLED, false),
            checkIntervalHours = prefs.getLong(
                KEY_INTERVAL,
                ScheduleChangeScheduler.DEFAULT_CHECK_INTERVAL_HOURS,
            ),
            totalChecks = prefs.getInt(KEY_TOTAL_CHECKS, 0),
            totalChangesDetected = prefs.getInt(KEY_TOTAL_CHANGES, 0),
        )
    )
    val state: StateFlow<ScheduleNotificationSettingsState> = _state

    fun toggleNotifications(enabled: Boolean) {
        Timber.d("Toggle notifications: $enabled")
        prefs.edit { putBoolean(KEY_ENABLED, enabled) }
        _state.value = _state.value.copy(notificationsEnabled = enabled)
        if (enabled) {
            scheduler.startPeriodicChecks(_state.value.checkIntervalHours)
        } else {
            scheduler.stopPeriodicChecks()
        }
    }

    fun setCheckInterval(hours: Long) {
        val interval = hours.coerceIn(
            ScheduleChangeScheduler.MIN_CHECK_INTERVAL_HOURS,
            ScheduleChangeScheduler.MAX_CHECK_INTERVAL_HOURS,
        )
        Timber.d("Set check interval: $interval hours")
        prefs.edit { putLong(KEY_INTERVAL, interval) }
        _state.value = _state.value.copy(checkIntervalHours = interval)
        if (_state.value.notificationsEnabled) {
            scheduler.startPeriodicChecks(interval)
        }
    }

    fun checkNow() {
        if (_state.value.isCheckingNow) return
        Timber.d("Manual check triggered")
        _state.value = _state.value.copy(isCheckingNow = true)
        scheduler.triggerImmediateCheck()
        scope.launch {
            // The worker runs in the background; show the spinner briefly and record the attempt
            delay(CHECK_INDICATOR_MS)
            val checks = _state.value.totalChecks + 1
            prefs.edit { putInt(KEY_TOTAL_CHECKS, checks) }
            _state.value = _state.value.copy(
                isCheckingNow = false,
                totalChecks = checks,
                lastCheckTime = LocalTime.now().format(TIME_FORMATTER),
            )
        }
    }

    private companion object {
        const val PREFS_NAME = "schedule_notification_settings"
        const val KEY_ENABLED = "enabled"
        const val KEY_INTERVAL = "interval_hours"
        const val KEY_TOTAL_CHECKS = "total_checks"
        const val KEY_TOTAL_CHANGES = "total_changes"
        const val CHECK_INDICATOR_MS = 1500L
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
