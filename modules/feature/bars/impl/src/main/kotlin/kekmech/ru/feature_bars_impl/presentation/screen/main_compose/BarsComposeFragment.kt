package kekmech.ru.feature_bars_impl.presentation.screen.main_compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class BarsComposeFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        MpeixTheme {
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
