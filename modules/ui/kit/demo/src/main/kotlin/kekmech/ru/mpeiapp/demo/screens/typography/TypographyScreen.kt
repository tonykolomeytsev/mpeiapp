package kekmech.ru.mpeiapp.demo.screens.typography

import androidx.compose.runtime.Composable
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kotlinx.serialization.Serializable

@Serializable
internal object TypographyScreen : NavScreen {

    @Composable
    override fun Content() {
        TypographyScreen()
    }
}


@Composable
private fun TypographyScreen() {
    UiKitScreen(title = "Typography") {
        /* no-op */
    }
}
