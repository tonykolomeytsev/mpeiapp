package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

internal class EolFragment : ComposeFragment() {

    private val router: Router by inject()

    @Composable
    override fun Screen() {
        MpeixTheme {
            EolScreen(
                onBackClick = { router.executeCommand(PopBackStack()) }
            )
        }
    }
}