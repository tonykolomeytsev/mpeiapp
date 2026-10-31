package kekmech.ru.debug_menu

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kekmech.ru.debug_menu.presentation.DebugMenuService
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import timber.log.Timber

internal class DebugMenuInitializer : AppLifecycleObserver {

    // Permission dialog pauses and resumes activity, so ask only once per process
    private var isNotificationPermissionRequested = false

    override fun onCreate(context: Context) {
        val application = context.applicationContext as Application
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {

                override fun onActivityPaused(activity: Activity) {
                    application.stopService(Intent(application, DebugMenuService::class.java))
                }

                override fun onActivityResumed(activity: Activity) {
                    requestNotificationPermissionIfNeeded(activity)
                    try {
                        application.startService(Intent(application, DebugMenuService::class.java))
                    } catch (e: Exception) {
                        Timber.e("Unable to start debug menu service: $e")
                    }
                }

                override fun onActivityDestroyed(activity: Activity) = Unit

                override fun onActivityStarted(activity: Activity) = Unit

                override fun onActivityStopped(activity: Activity) = Unit

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) = Unit

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) = Unit
            }
        )
    }

    /**
     * Since Android 13 debug menu notification is hidden without runtime permission
     */
    private fun requestNotificationPermissionIfNeeded(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (isNotificationPermissionRequested) return
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) return
        isNotificationPermissionRequested = true
        ActivityCompat.requestPermissions(activity, arrayOf(permission), NotificationPermissionRequestCode)
    }

    private companion object {

        const val NotificationPermissionRequestCode = 0xDEB
    }
}
