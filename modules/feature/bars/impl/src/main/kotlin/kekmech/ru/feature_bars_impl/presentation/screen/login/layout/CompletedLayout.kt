package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun CompletedLayout() {
    StatusLayout(
        icon = Icons.Outlined.CheckCircle,
        iconTint = MpeixTheme.palette.primary,
        title = "Готово!",
        description = "Вы вошли в БАРС",
    )
}
