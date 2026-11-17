package kekmech.ru.ui_preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.result.LocalResultEventBus
import androidx.navigation3.runtime.result.rememberResultEventBus
import kekmech.ru.lib_navigation.ActivityCommand
import kekmech.ru.lib_navigation.Command
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Wrapper for Compose previews. Use it instead of [MpeixTheme] in `@Preview` functions.
 *
 * Provides the composition locals which are normally provided by the app's NavDisplay,
 * so screens using `LocalRouter.current` or `LocalResultEventBus.current` can be rendered in previews.
 */
@Composable
public fun MpeixPreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalRouter provides PreviewRouter,
        // There are no other screens to exchange results with in previews, so the bus just stays idle
        LocalResultEventBus provides rememberResultEventBus(),
    ) {
        MpeixTheme(darkTheme = darkTheme, content = content)
    }
}

/**
 * Router which does nothing. Navigation is not possible in previews anyway.
 */
private object PreviewRouter : Router {

    override val backStack: List<ScreenKey> = emptyList()

    override fun executeCommand(vararg commands: Command): Unit = Unit

    override fun executeCommand(command: ActivityCommand): Unit = Unit
}
