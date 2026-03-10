package kekmech.ru.lib_elm_compose

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.android.RetainedElmStore
import money.vivid.elmslie.android.RetainedElmStoreFactory
import money.vivid.elmslie.core.store.Store
import org.koin.compose.koinInject

/**
 * Inject ELM Store into any Composable screen.
 *
 * ## Example:
 *
 * Your screen's store factory:
 * ```kotlin
 * //
 * internal class MyStoreFactory(
 *     private val dependency1: DependencyType1,
 *     private val dependency2: DependencyType2,
 *     ...
 * ) {
 *
 *     fun create(arg1: String, arg2: Long): ElmStore<...> = ...
 * }
 * ```
 *
 * Your screen's Composable:
 * ```kotlin
 * @Composable
 * fun MyComposable(arg1: String, arg2: Long) {
 *     // instantiate store
 *     val store = rememberElmStore(MyStoreFactory::class) { create(arg1, arg2) }
 * }
 * ```
 */
@Composable
@Deprecated("Use rememberElmStore(key = \"\", ...) implementation")
public inline fun <reified StoreFactory : Any, Event : Any, Effect : Any, State : Any> rememberElmStore(
    crossinline factory: StoreFactory.() -> Store<Event, Effect, State>,
): Store<Event, Effect, State> {
    val factoryInstance = koinInject<StoreFactory>()
    return remember(StoreFactory::class) {
        factory
            .invoke(factoryInstance)
            .also { it.start() }
    }
}

@Composable
public fun <Event : Any, Effect : Any, State : Any> rememberElmStore(
    key: String,
    viewModelStoreOwner: ViewModelStoreOwner = LocalViewModelStoreOwner.current ?: error("No ViewModelStoreOwner"),
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current,
    saveState: Bundle.(State) -> Unit = {},
    storeFactory: SavedStateHandle.() -> Store<Event, Effect, State>
): Store<Event, Effect, State> {
    return remember(key) {
        val factory = RetainedElmStoreFactory(
            stateRegistryOwner = savedStateRegistryOwner,
            defaultArgs = Bundle.EMPTY,
            storeFactory = storeFactory,
            saveState = saveState,
        )
        val provider = ViewModelProvider(viewModelStoreOwner, factory)
        @Suppress("UNCHECKED_CAST")
        provider[key, RetainedElmStore::class.java].store as Store<Event, Effect, State>
    }
}

/**
 * Support function for ELM Store Effects handling.
 *
 * Usage:
 * ```kotlin
 * EffectHandler(store.effects()) { effect ->
 *     is Effect.MyEffect -> ...
 * }
 * ```
 */
@Composable
public fun <Effect : Any> EffectHandler(
    effects: Flow<Effect>,
    effectHandler: (Effect) -> Unit,
) {
    val currentEffectHandler by rememberUpdatedState(effectHandler)
    LaunchedEffect(Unit) {
        effects.collect { currentEffectHandler.invoke(it) }
    }
}
