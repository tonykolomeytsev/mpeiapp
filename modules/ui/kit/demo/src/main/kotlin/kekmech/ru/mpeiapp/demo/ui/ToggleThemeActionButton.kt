package kekmech.ru.mpeiapp.demo.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import kekmech.ru.mpeiapp.demo.R


@Composable
fun ToggleThemeActionButton() {
    val themeController = LocalUiKitThemeController.current
    IconButton(
        onClick = { themeController.toggleDarkTheme() },
    ) {
        Icon(
            painter = if (themeController.darkThemeEnabled()) {
                painterResource(R.drawable.ic_light_mode_24)
            } else {
                painterResource(R.drawable.ic_dark_mode_24)
            },
            contentDescription = null,
        )
    }
}
