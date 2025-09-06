package kekmech.ru.debug_menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

internal interface NavScreen : NavKey {

    @Composable
    fun Content()
}
