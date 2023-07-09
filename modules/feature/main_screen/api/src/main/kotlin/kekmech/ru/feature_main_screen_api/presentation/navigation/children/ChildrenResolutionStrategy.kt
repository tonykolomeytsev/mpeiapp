package kekmech.ru.feature_main_screen_api.presentation.navigation.children

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab

interface ChildrenResolutionStrategy {

    fun resolve(navTarget: MainScreenTab, buildContext: BuildContext): Node
}
