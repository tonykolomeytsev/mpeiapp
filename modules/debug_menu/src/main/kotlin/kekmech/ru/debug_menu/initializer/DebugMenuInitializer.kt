package kekmech.ru.debug_menu.initializer

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kekmech.ru.common_app_lifecycle.AppLifecycleObserver
import timber.log.Timber

internal class DebugMenuInitializer : AppLifecycleObserver {

    override fun onCreate(context: Context) {
        val application = context.applicationContext as Application
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {

                override fun onActivityPaused(activity: Activity) {
                    application.stopService(Intent(application, DebugMenuService::class.java))
                }

                override fun onActivityResumed(activity: Activity) {
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
}
