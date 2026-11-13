package kekmech.ru.feature_app_update_impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_app_update_impl.navigation.ForceUpdateScreenKey
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_navigation.PopBackStack

@Composable
internal fun ForceUpdateEntry(key: ForceUpdateScreenKey) {
    val analytics = rememberScreenAnalytics("ForceUpdate")
    val context = LocalContext.current
    val router = LocalRouter.current
    ForceUpdateScreen(
        description = key.updateInfo.shortDescription,
        onUpdateClick = {
            analytics.sendClick("UpdateNow")
            context.openLinkExternal(key.updateInfo.updateUrl)
        },
        onLaterClick = {
            analytics.sendClick("UpdateLater")
            router.executeCommand(PopBackStack())
        },
    )
}
