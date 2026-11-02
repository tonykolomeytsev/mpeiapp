package kekmech.ru.lib_analytics_android.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kekmech.ru.lib_analytics_android.Analytics
import kekmech.ru.lib_analytics_android.AnalyticsImpl
import kekmech.ru.lib_analytics_android.AnalyticsWrapper
import org.koin.compose.koinInject

/**
 * Compose-аналог [screenAnalytics]: отправляет показ экрана и изменения его видимости
 * по lifecycle текущего экрана.
 */
@Composable
public fun rememberScreenAnalytics(screenName: String): Analytics {
    val analyticsWrapper = koinInject<AnalyticsWrapper>()
    val analytics = remember(screenName) { AnalyticsImpl(analyticsWrapper, screenName) }
    // Экран возвращается в композицию при каждом возврате по back stack'у,
    // а показ экрана должен отправиться один раз, как в onCreate фрагмента
    var isScreenShownSent by rememberSaveable(screenName) { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, analytics) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> if (!isScreenShownSent) {
                    isScreenShownSent = true
                    analytics.sendScreenShown()
                }
                Lifecycle.Event.ON_RESUME -> analytics.sendScreenVisibilityChanged(true)
                Lifecycle.Event.ON_PAUSE -> analytics.sendScreenVisibilityChanged(false)
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    return analytics
}
