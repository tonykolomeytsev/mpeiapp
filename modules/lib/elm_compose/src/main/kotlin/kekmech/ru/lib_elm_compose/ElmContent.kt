package kekmech.ru.lib_elm_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import kotlinx.coroutines.flow.Flow
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
@Deprecated("Use another ElmContent implementation")
public inline fun <reified StoreFactory : Any, Event : Any, Effect : Any, State : Any> ElmContent(
    crossinline factory: StoreFactory.() -> Store<Event, Effect, State>,
    crossinline composable: @Composable (
        onAccept: (Event) -> Unit,
        state: State,
        effects: Flow<Effect>,
        modifier: Modifier,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = rememberElmStore(factory)
    val state by store.states.collectAsState()
    val onAccept = remember {
        { event: Event -> store.accept(event) }
    }
    composable.invoke(onAccept, state, store.effects, modifier)
}

@Composable
public inline fun <Event : Any, Effect : Any, State : Any> ElmContent(
    key: String,
    noinline storeFactory: SavedStateHandle.() -> Store<Event, Effect, State>,
    viewModelStoreOwner: ViewModelStoreOwner = LocalViewModelStoreOwner.current ?: error("No ViewModelStoreOwner"),
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current,
    content: @Composable (onAccept: (Event) -> Unit, state: State, effects: Flow<Effect>) -> Unit,
) {
    val store = rememberElmStore(
        key = key,
        viewModelStoreOwner = viewModelStoreOwner,
        savedStateRegistryOwner = savedStateRegistryOwner,
        storeFactory = storeFactory,
    )
    val state by store.states.collectAsState()
    val onAccept = remember {
        { event: Event -> store.accept(event) }
    }
    val effects = store.effects
    content(onAccept, state, effects)
}