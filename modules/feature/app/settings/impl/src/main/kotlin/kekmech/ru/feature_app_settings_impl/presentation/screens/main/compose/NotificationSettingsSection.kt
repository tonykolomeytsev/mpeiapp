package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_switch.Switch

/**
 * "Notifications" section of the settings: both kinds of notifications can be switched on / off right here.
 * Switching on asks for the system notification permission (Android 13+) first.
 */
@Composable
internal fun NotificationSettingsSection(
    settings: ScheduleNotificationSettings,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state by settings.observe().collectAsState(initial = settings.current())

    // what to do when the user grants the permission requested by a switch
    var pendingEnable by remember { mutableStateOf<(() -> Unit)?>(null) }
    var deniedMessage by remember { mutableStateOf(Strings.class_reminder_permission_denied) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val action = pendingEnable
        pendingEnable = null
        if (granted) {
            action?.invoke()
        } else {
            Toast.makeText(context, deniedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun toggle(enabled: Boolean, @StringRes onDenied: Int, apply: (Boolean) -> Unit) {
        if (enabled && !context.hasNotificationPermission()) {
            deniedMessage = onDenied
            pendingEnable = { apply(true) }
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            apply(enabled)
        }
    }

    Column(modifier = modifier) {
        SettingsSectionHeader(title = stringResource(Strings.app_settings_header_notifications))
        ListItem(
            headlineText = stringResource(Strings.app_settings_notify_reminders_title),
            supportingText = stringResource(Strings.app_settings_notify_reminders_description),
            trailingContent = {
                Switch(
                    checked = state.classReminders,
                    onCheckedChange = { enabled ->
                        toggle(enabled, Strings.class_reminder_permission_denied, settings::setClassRemindersEnabled)
                    },
                )
            },
        )
        ListItem(
            headlineText = stringResource(Strings.app_settings_notify_changes_title),
            supportingText = stringResource(Strings.app_settings_notify_changes_description),
            trailingContent = {
                Switch(
                    checked = state.scheduleChanges,
                    onCheckedChange = { enabled ->
                        toggle(
                            enabled,
                            Strings.schedule_notifications_permission_denied,
                            settings::setScheduleChangesEnabled,
                        )
                    },
                )
            },
        )
        ListItem(
            headlineText = stringResource(Strings.app_settings_notify_lock_screen_title),
            supportingText = stringResource(Strings.app_settings_notify_lock_screen_description),
            trailingContent = {
                Switch(
                    checked = state.lockScreenSchedule,
                    onCheckedChange = { enabled ->
                        toggle(
                            enabled,
                            Strings.lock_screen_permission_denied,
                            settings::setLockScreenScheduleEnabled,
                        )
                    },
                )
            },
        )
        ListItem(
            headlineText = stringResource(Strings.app_settings_notify_more_title),
            supportingText = stringResource(Strings.app_settings_notify_more_description),
            modifier = Modifier.clickable(onClick = onOpenDetails),
        )
    }
}

private fun Context.hasNotificationPermission(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
