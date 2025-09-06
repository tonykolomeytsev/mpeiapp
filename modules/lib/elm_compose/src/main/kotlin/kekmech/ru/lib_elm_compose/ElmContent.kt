package kekmech.ru.lib_elm_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import money.vivid.elmslie.core.store.Store

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
 */
@Composable
public inline fun <reified StoreFactory : Any, Event : Any, Effect : Any, State : Any> ElmContent(
    crossinline factory: StoreFactory.() -> Store<Event, Effect, State>,
    crossinline composable: @Composable (
        onAccept: (Event) -> Unit,
        state: State,
        modifier: Modifier,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = rememberElmStore(factory)
    val state by store.states.collectAsState()
    val onAccept = remember {
        { event: Event -> store.accept(event) }
    }
    composable.invoke(onAccept, state, modifier)
}
