package kekmech.ru.lib_elm_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
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
 *
 * For ELM Screens, please, use [elmNode] wherever possible.
 *
 * @see elmNode
 */
@Composable
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
