package kekmech.ru.feature_main_screen_impl.presentation.navigation.children

import com.bumble.appyx.core.plugin.NodeAware
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenDependencyComponent
import kekmech.ru.feature_main_screen_impl.presentation.screen.main.MainScreenNode

internal class DependencyInjectionPlugin : NodeAware<MainScreenNode> {

    override lateinit var node: MainScreenNode
        private set

    override fun init(node: MainScreenNode) {
        this.node = node
        val component = (node.parent as? MainScreenDependencyComponent)
            ?: error("Parent node `${node.parent}` should implement MainScreenDependencyComponent")
        node.resolutionStrategy = component.resolutionStrategy
    }
}
