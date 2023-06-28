package kekmech.ru.common_elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import vivid.money.elmslie.core.store.Store

abstract class ElmNavTarget<Event : Any, Effect : Any, State : Any> : NavTarget {

    inline fun <reified Factory : Any> elmNode(
        buildContext: BuildContext,
        crossinline factory: Factory.() -> Store<Event, Effect, State>,
        crossinline composable: @Composable (Modifier, Store<Event, Effect, State>, State) -> Unit,
    ): Node =
        node(buildContext) {
            val store = rememberElmStore(factory)
            val state by store.asState()
            composable.invoke(it, store, state)
        }
}

