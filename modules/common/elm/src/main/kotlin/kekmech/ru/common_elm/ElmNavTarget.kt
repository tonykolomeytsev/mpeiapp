package kekmech.ru.common_elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import vivid.money.elmslie.core.store.Store

/**
 * Extension for creating Appyx nodes with ELM Store.
 *
 * Usage in `NavTarget`:
 *
 * ```kotlin
 * @Parcelize
 * internal class MyScreenNavTarget(
 *     private val myArg: Int,
 * ) : NavTarget {
 *
 *     override fun resolve(buildContext: BuildContext): Node =
 *         elmNode<ElmDemoStoreFactory, _, _, _>(
 *             buildContext = buildContext,
 *             factory = { create(myArg = myArg) },
 *         ) { _, store, state -> MyScreen(store = store, state = state) }
 * }
 * ```
 *
 * @see com.bumble.appyx.core.node.node
 * @see com.bumble.appyx.core.node.Node
 */
inline fun <reified Factory : Any, Event : Any, Effect : Any, State : Any> elmNode(
    buildContext: BuildContext,
    crossinline factory: Factory.() -> Store<Event, Effect, State>,
    crossinline composable: @Composable (Modifier, Store<Event, Effect, State>, State) -> Unit,
): Node =
    node(buildContext) {
        val store = rememberElmStore(factory)
        val state by store.asState()
        composable.invoke(it, store, state)
    }
