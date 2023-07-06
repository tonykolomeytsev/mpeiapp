package kekmech.ru.lib_elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import vivid.money.elmslie.core.store.Store
import kotlin.reflect.KClass

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
 *         elmNode(
 *             buildContext = buildContext,
 *             storeFactoryClass = MyStoreFactory::class,
 *             factory = { create(myArg = myArg) },
 *         ) { store, state, modifier -> MyScreen(store, state, modifier) }
 * }
 * ```
 *
 * @see com.bumble.appyx.core.node.node
 * @see com.bumble.appyx.core.node.Node
 */
inline fun <StoreFactory : Any, Event : Any, Effect : Any, State : Any> elmNode(
    buildContext: BuildContext,
    storeFactoryClass: KClass<StoreFactory>,
    crossinline factory: StoreFactory.() -> Store<Event, Effect, State>,
    crossinline composable: @Composable (
        store: Store<Event, Effect, State>,
        state: State,
        modifier: Modifier,
    ) -> Unit,
): Node =
    node(buildContext) { modifier ->
        val store = rememberElmStore(storeFactoryClass, factory)
        val state by store.states().collectAsState(initial = store.currentState)
        composable.invoke(store, state, modifier)
    }
