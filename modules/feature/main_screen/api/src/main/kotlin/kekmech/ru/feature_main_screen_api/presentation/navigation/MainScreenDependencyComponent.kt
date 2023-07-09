package kekmech.ru.feature_main_screen_api.presentation.navigation

import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy

interface MainScreenDependencyComponent {

    val resolutionStrategy: ChildrenResolutionStrategy
}
