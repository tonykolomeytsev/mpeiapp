package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class BarsLoginFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        MpeixTheme {
            val factory = koinInject<BarsLoginStoreFactory>()
            ElmContent(
                key = "BarsLoginScreen",
                storeFactory = { factory.create() },
                content = ::BarsLoginScreen,
            )
        }
    }
}
