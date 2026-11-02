package kekmech.ru.mpeiapp.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent
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

// Duration of the predictive back animation after the finger is released
private const val PREDICTIVE_BACK_DURATION_MS = 300

// The revealed screen starts 1/4 of the width away
private const val PARALLAX_DIVIDER = 4

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
            predictivePopTransitionSpec = { swipeEdge -> predictivePopTransition(swipeEdge) },
            entryProvider = entryProvider,
        )
    }
}

private fun AnimatedContentTransitionScope<Scene<ScreenKey>>.screenTransition(): ContentTransform {
    // Switching between tabs is instant, like it was with the bottom navigation view
    return if (isTabSwitch()) {
        EnterTransition.None togetherWith ExitTransition.None
    } else {
        fadeIn(tween(TRANSITION_DURATION_MS)) togetherWith fadeOut(tween(TRANSITION_DURATION_MS))
    }
}

/**
 * Closing screen follows the thumb: while the gesture is in progress NavDisplay seeks this
 * transition by the gesture progress, so linear easing keeps the screen right under the finger.
 */
private fun AnimatedContentTransitionScope<Scene<ScreenKey>>.predictivePopTransition(
    @NavigationEvent.SwipeEdge swipeEdge: Int,
): ContentTransform {
    if (isTabSwitch()) return EnterTransition.None togetherWith ExitTransition.None
    // Swipe from the right edge moves the screen to the left, otherwise to the right
    val direction = if (swipeEdge == NavigationEvent.EDGE_RIGHT) -1 else 1
    val spec = tween<IntOffset>(PREDICTIVE_BACK_DURATION_MS, easing = LinearEasing)
    return ContentTransform(
        // Previous screen moves slightly from behind, like in the system back animation
        targetContentEnter = slideInHorizontally(spec) { width -> -direction * width / PARALLAX_DIVIDER },
        initialContentExit = slideOutHorizontally(spec) { width -> direction * width },
        // Closing screen stays on top of the screen it reveals
        targetContentZIndex = -1f,
    )
}

private fun AnimatedContentTransitionScope<Scene<ScreenKey>>.isTabSwitch(): Boolean =
    initialState.entries.lastOrNull()?.bottomTab != null &&
        targetState.entries.lastOrNull()?.bottomTab != null
