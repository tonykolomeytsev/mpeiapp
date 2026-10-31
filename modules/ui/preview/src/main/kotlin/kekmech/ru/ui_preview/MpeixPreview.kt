package kekmech.ru.ui_preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentManager
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultController
import kekmech.ru.lib_navigation.ActivityCommand
import kekmech.ru.lib_navigation.Command
import kekmech.ru.lib_navigation.Router
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Wrapper for Compose previews. Use it instead of [MpeixTheme] in `@Preview` functions.
 *
 * Provides stubs for composition locals which are normally provided by the host Fragment,
 * so screens using `LocalRouter.current` or `LocalResultController.current` can be rendered in previews.
 */
@Composable
public fun MpeixPreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalRouter provides PreviewRouter,
        LocalResultController provides PreviewResultController,
    ) {
        MpeixTheme(darkTheme = darkTheme, content = content)
    }
}

/**
 * Router which does nothing. Navigation is not possible in previews anyway.
 */
private object PreviewRouter : Router {

    override fun executeCommand(vararg commands: Command): Unit = Unit

    override fun executeCommand(command: ActivityCommand): Unit = Unit

    override fun executeCommand(fragmentManager: FragmentManager, command: Command): Unit = Unit
}

/**
 * Result controller which does nothing. There are no other screens to exchange results with in previews.
 */
private object PreviewResultController : ResultController {

    override fun <T : Serializable> setResult(requestKey: String, result: T): Unit = Unit

    override fun <T : Serializable> setResultListener(
        requestKey: String,
        resultClass: KClass<T>,
        listener: (T) -> Unit,
    ): Unit = Unit

    override fun clearResultListener(requestKey: String): Unit = Unit
}
