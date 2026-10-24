package kekmech.ru.feature_schedule_impl.notifications

import androidx.compose.foundation.isSystemInDarkTheme
import kekmech.ru.feature_app_settings_api.domain.model.isDarkTheme
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_android.close
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.theme.MpeixScreenTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

/**
 * Screen with settings of schedule change notifications.
 * Asks for POST_NOTIFICATIONS permission (Android 13+) before enabling the feature.
 */
internal class ScheduleNotificationSettingsFragment : ComposeFragment() {

    private val viewModel by inject<ScheduleNotificationSettingsViewModel>()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.toggleNotifications(true)
        } else {
            Toast
                .makeText(
                    requireContext(),
                    Strings.schedule_notifications_permission_denied,
                    Toast.LENGTH_LONG,
                )
                .show()
        }
    }

    private val reminderPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.refresh()
        if (!granted) {
            Toast
                .makeText(
                    requireContext(),
                    Strings.class_reminder_permission_denied,
                    Toast.LENGTH_LONG,
                )
                .show()
        }
    }

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val isSystemDark = isSystemInDarkTheme()
        val darkTheme = remember(isSystemDark) { derivedStateOf { appSettings.getAppSettings().isDarkTheme(isSystemDark) } }

        MpeixScreenTheme(darkTheme = darkTheme.value) {
            ScheduleNotificationSettingsScreen(
                viewModel = viewModel,
                onToggleNotifications = ::onToggleNotifications,
                onRequestNotificationPermission = ::requestReminderNotificationPermission,
                onOpenExactAlarmSettings = ::openExactAlarmSettings,
                onBackClick = { close() },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // permissions are changed in the system settings: re-read them on return
        viewModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    private fun onToggleNotifications(enabled: Boolean) {
        if (enabled && !hasNotificationPermission()) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.toggleNotifications(enabled)
        }
    }

    private fun requestReminderNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission()) {
            reminderPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // notifications are disabled for the whole app in the system settings
            startActivity(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            )
        }
    }

    private fun openExactAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(
                Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:${requireContext().packageName}"),
                )
            )
        }
    }

    private fun hasNotificationPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
}
