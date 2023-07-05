package kekmech.ru.lib_analytics_android

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.lib_network.device_id.DeviceIdProvider

internal class AnalyticsInitializer(
    private val deviceIdProvider: DeviceIdProvider,
) : MainActivityLifecycleObserver {

    override fun onCreate(context: Context) {
        FirebaseAnalytics.getInstance(context).apply {
            setUserId(deviceIdProvider.getDeviceId())
        }
    }
}
