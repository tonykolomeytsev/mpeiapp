package kekmech.ru.mpeiapp

import android.Manifest
import android.app.Application
import android.os.Build
import androidx.test.runner.AndroidJUnitRunner
import kekmech.ru.mpeiapp.testing.editAppPreferences

/**
 * # MpeixTestRunner
 *
 * Runs the UI tests against the in-app mock backend (`modules/mock_server`), so they don't depend
 * on the network and the real schedules.
 *
 * `MpeixApp.onCreate` reads the backend environment before any test code runs, so the settings
 * every test needs are written here. Orchestrator wipes the app data before each test
 * (`clearPackageData`) and runs each test in a new process, so this runs again for every test.
 *
 * The settings of a single test (e.g. the selected schedule) go in rules, see `SelectedScheduleRule`.
 */
class MpeixTestRunner : AndroidJUnitRunner() {

    override fun callApplicationOnCreate(app: Application) {
        app.editAppPreferences {
            // MockServer starts with the app, the requests go to localhost:8080
            putString("app_env", "MOCK")
            // Snowfall requests a new frame forever, Compose never becomes idle and the tests hang
            putBoolean("app_is_snow_enabled", false)
        }
        grantNotificationPermission(app.packageName)
        super.callApplicationOnCreate(app)
    }

    /**
     * The debug menu asks for this permission when the app starts,
     * the system dialog would cover the app
     */
    private fun grantNotificationPermission(packageName: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        uiAutomation.grantRuntimePermission(packageName, Manifest.permission.POST_NOTIFICATIONS)
    }
}
