package kekmech.ru.mpeiapp.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.rememberResultControllerNavEntryDecorator
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomTab
import kekmech.ru.mpeiapp.ui.main.rememberMainTabsNavEntryDecorator

// Same as the old fragment transactions (anim_in/anim_out)
private const val TRANSITION_DURATION_MS = 100

/**
 * Корень приложения: все экраны живут в одном back stack'е [Router].
 */
@Composable
fun App(
    router: Router,
    navEntries: List<NavEntries>,
) {
    // Back stack пуст только пока MainActivity не открыла первый экран
    if (router.backStack.isEmpty()) return
    val activity = LocalActivity.current as FragmentActivity
    val entryProvider = remember(navEntries) {
        entryProvider<ScreenKey> {
            navEntries.forEach { with(it) { register() } }
        }
    }
    CompositionLocalProvider(LocalRouter provides router) {
        NavDisplay(
            backStack = router.backStack,
            onBack = { router.executeCommand(PopBackStack()) },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                rememberResultControllerNavEntryDecorator(activity),
                rememberMainTabsNavEntryDecorator(),
            ),
            transitionSpec = { screenTransition() },
            popTransitionSpec = { screenTransition() },
            predictivePopTransitionSpec = { screenTransition() },
            entryProvider = entryProvider,
        )
    }
}

private fun AnimatedContentTransitionScope<Scene<ScreenKey>>.screenTransition(): ContentTransform {
    // Switching between tabs is instant, like it was with the bottom navigation view
    val isTabSwitch = initialState.entries.lastOrNull()?.bottomTab != null &&
        targetState.entries.lastOrNull()?.bottomTab != null
    return if (isTabSwitch) {
        EnterTransition.None togetherWith ExitTransition.None
    } else {
        fadeIn(tween(TRANSITION_DURATION_MS)) togetherWith fadeOut(tween(TRANSITION_DURATION_MS))
    }
}
