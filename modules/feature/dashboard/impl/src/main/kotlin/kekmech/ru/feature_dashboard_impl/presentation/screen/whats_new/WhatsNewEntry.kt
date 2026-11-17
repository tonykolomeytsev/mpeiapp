package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new

import androidx.compose.runtime.Composable
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack

@Composable
internal fun WhatsNewEntry() {
    rememberScreenAnalytics("WhatsNew")
    val router = LocalRouter.current
    WhatsNewScreen(
        onBackClick = { router.executeCommand(PopBackStack()) },
    )
}
