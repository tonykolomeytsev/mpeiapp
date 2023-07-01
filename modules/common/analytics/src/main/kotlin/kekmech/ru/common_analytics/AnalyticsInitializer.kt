package kekmech.ru.common_analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.common_network.device_id.DeviceIdProvider

internal class AnalyticsInitializer(
    private val deviceIdProvider: DeviceIdProvider,
) : MainActivityLifecycleObserver {

    override fun onCreate(context: Context) {
        FirebaseAnalytics.getInstance(context).apply {
            setUserId(deviceIdProvider.getDeviceId())
        }
    }
}
