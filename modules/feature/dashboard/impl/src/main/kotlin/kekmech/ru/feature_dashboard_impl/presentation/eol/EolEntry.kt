package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun EolEntry() {
    MpeixTheme {
        val router = LocalRouter.current
        EolScreen(
            onBackClick = { router.executeCommand(PopBackStack()) }
        )
    }
}
