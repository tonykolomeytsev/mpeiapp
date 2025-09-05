package kekmech.ru.common_analytics.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper
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
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                analytics.sendScreenShown()
            }

            override fun onResume(owner: LifecycleOwner) {
                analytics.sendScreenVisibilityChanged(true)
            }

            override fun onPause(owner: LifecycleOwner) {
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
