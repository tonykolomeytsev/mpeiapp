package kekmech.ru.feature_bars_impl.presentation.screen.main_compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class BarsComposeFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val darkTheme =
            remember { derivedStateOf { appSettings.getAppSettings().isDarkThemeEnabled } }
        MpeixTheme(darkTheme = darkTheme.value) {
            val factory = koinInject<BarsComposeStoreFactory>()
            val activity = LocalActivity.current as ComponentActivity
            ElmContent(
                key = "BarsScreen",
                storeFactory = { factory.create() },
                viewModelStoreOwner = activity,
                savedStateRegistryOwner = activity,
                content = ::BarsComposeScreen,
            )
        }
    }
}
