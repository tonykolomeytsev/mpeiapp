package kekmech.ru.lib_analytics_android.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kekmech.ru.lib_analytics_android.Analytics
import kekmech.ru.lib_analytics_android.AnalyticsImpl
import kekmech.ru.lib_analytics_android.AnalyticsWrapper
import org.koin.android.ext.android.inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public class ScreenAnalyticsDelegate<T : Analytics>(
    fragment: Fragment,
    analyticsProvider: () -> T
) : ReadOnlyProperty<Fragment, T> {

    private val analytics by lazy(LazyThreadSafetyMode.NONE) { analyticsProvider() }

    init {
        @Suppress("UNUSED")
        fragment.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                analytics.sendScreenShown()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                analytics.sendScreenVisibilityChanged(true)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                analytics.sendScreenVisibilityChanged(false)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return analytics
    }
}

public fun Fragment.screenAnalytics(screenName: String): ScreenAnalyticsDelegate<Analytics> {
    val analyticsWrapper by inject<AnalyticsWrapper>()
    return ScreenAnalyticsDelegate(this) { AnalyticsImpl(analyticsWrapper, screenName) }
}
