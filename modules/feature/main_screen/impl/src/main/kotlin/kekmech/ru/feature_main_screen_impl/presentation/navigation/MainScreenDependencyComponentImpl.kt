package kekmech.ru.feature_main_screen_impl.presentation.navigation

import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenDependencyComponent
import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy

internal class MainScreenDependencyComponentImpl(
    override val resolutionStrategy: ChildrenResolutionStrategy,
) : MainScreenDependencyComponent
