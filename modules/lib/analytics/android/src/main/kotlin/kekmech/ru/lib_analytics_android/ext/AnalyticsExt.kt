package kekmech.ru.lib_analytics_android.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
        fragment.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> analytics.sendScreenShown()
                    Lifecycle.Event.ON_RESUME -> analytics.sendScreenVisibilityChanged(true)
                    Lifecycle.Event.ON_PAUSE -> analytics.sendScreenVisibilityChanged(false)
                    else -> {
                        /* no-op */
                    }
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
