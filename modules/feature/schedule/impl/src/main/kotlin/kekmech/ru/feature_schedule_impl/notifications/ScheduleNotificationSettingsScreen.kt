package kekmech.ru.feature_schedule_impl.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Settings screen for schedule change notifications
 */
@Composable
internal fun ScheduleNotificationSettingsScreen(
    viewModel: ScheduleNotificationSettingsViewModel,
    onToggleNotifications: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Strings.schedule_notifications_title),
                navigationIcon = { BackIconButton(onClick = onBackClick) },
            )
        },
        containerColor = MpeixTheme.palette.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NotificationToggleCard(
                enabled = state.notificationsEnabled,
                onToggle = onToggleNotifications,
            )

            if (state.notificationsEnabled) {
                CheckIntervalCard(
                    intervalHours = state.checkIntervalHours,
                    onIntervalChange = { viewModel.setCheckInterval(it) },
                )
                ImmediateCheckCard(
                    isChecking = state.isCheckingNow,
                    lastCheckTime = state.lastCheckTime,
                    onCheckNow = { viewModel.checkNow() },
                )
                StatisticsCard(
                    totalChecks = state.totalChecks,
                    changesDetected = state.totalChangesDetected,
                )
            }
        }
    }
}

@Composable
private fun NotificationToggleCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Strings.schedule_notifications_toggle_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(Strings.schedule_notifications_toggle_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
            )
        }
    }
}

@Composable
private fun CheckIntervalCard(
    intervalHours: Long,
    onIntervalChange: (Long) -> Unit,
) {
    // Local slider position: WorkManager is rescheduled only when the user lifts the finger
    var sliderValue by remember(intervalHours) { mutableFloatStateOf(intervalHours.toFloat()) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Strings.schedule_notifications_interval_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(
                    Strings.schedule_notifications_interval_value,
                    sliderValue.toInt(),
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                onValueChangeFinished = { onIntervalChange(sliderValue.toLong()) },
                valueRange = 1f..24f,
                steps = 22,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Strings.schedule_notifications_interval_min),
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = stringResource(Strings.schedule_notifications_interval_max),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun ImmediateCheckCard(
    isChecking: Boolean,
    lastCheckTime: String?,
    onCheckNow: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Strings.schedule_notifications_check_now_title),
                style = MaterialTheme.typography.titleMedium,
            )
            if (lastCheckTime != null) {
                Text(
                    text = stringResource(Strings.schedule_notifications_last_check, lastCheckTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Button(
                onClick = onCheckNow,
                enabled = !isChecking,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isChecking) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Strings.schedule_notifications_checking))
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Strings.schedule_notifications_check_button))
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    totalChecks: Int,
    changesDetected: Int,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Strings.schedule_notifications_stats_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = stringResource(Strings.schedule_notifications_stats_checks),
                    value = totalChecks.toString(),
                )
                StatItem(
                    icon = Icons.Default.Notifications,
                    label = stringResource(Strings.schedule_notifications_stats_changes),
                    value = changesDetected.toString(),
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
