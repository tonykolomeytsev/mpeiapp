package kekmech.ru.mpeiapp.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackFader
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.BackStackNavigator
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.mpeiapp.demo.screens.main.MainScreenNavTarget

internal class BackStackRootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = MainScreenNavTarget(),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<NavTarget>(
    navModel = backStack,
    buildContext = buildContext,
) {

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        navTarget.resolve(buildContext)

    @Composable
    override fun View(modifier: Modifier) {

        val backStackNavigator = remember { BackStackNavigator(backStack) }

        CompositionLocalProvider(
          LocalBackStackNavigator provides backStackNavigator,
        ) {
            Children(
                navModel = backStack,
                transitionHandler = rememberBackstackFader(),
            )
        }
    }
}
