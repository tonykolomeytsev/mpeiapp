package kekmech.ru.feature_app_update_impl.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_app_update_impl.navigation.BlockingUpdateScreenKey
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val LINK_GITHUB = "https://github.com/tonykolomeytsev/mpeiapp/issues"

@Composable
internal fun BlockingUpdateEntry(key: BlockingUpdateScreenKey) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    MpeixTheme {
        BlockingUpdateScreen(
            link = LINK_GITHUB,
            // Экран блокирует все приложение, поэтому выход с него - выход из приложения
            onExitClick = { activity?.finish() },
            onUpdateClick = { context.openLinkExternal(key.updateInfo.updateUrl) },
            onLinkClick = { context.openLinkExternal(LINK_GITHUB) },
        )
    }
}
