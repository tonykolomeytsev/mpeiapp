package kekmech.ru.debug_menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

internal val LocalNavBackStack = compositionLocalOf<NavBackStack<NavKey>> { error("Not provided") }

@Composable
fun WithBackStack(backStack: NavBackStack<NavKey>, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavBackStack provides backStack, content = content)
}
