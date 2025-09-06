package kekmech.ru.mpeiapp.demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack

internal val LocalNavBackStack = compositionLocalOf<NavBackStack> { error("Not provided") }

@Composable
fun WithBackStack(backStack: NavBackStack, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavBackStack provides backStack, content = content)
}
