package kekmech.ru.common_elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import org.koin.compose.LocalKoinScope
import vivid.money.elmslie.core.store.Store
import kotlin.reflect.KClass

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
inline fun <StoreFactory : Any, Event : Any, Effect : Any, State : Any> rememberElmStore(
    storeFactoryClass: KClass<StoreFactory>,
    crossinline factory: StoreFactory.() -> Store<Event, Effect, State>,
): Store<Event, Effect, State> {
    val koinScope = LocalKoinScope.current
    val storeFactory: StoreFactory = remember(storeFactoryClass, koinScope) {
        koinScope.get(
            clazz = storeFactoryClass,
            qualifier = null,
            parameters = null,
        )
    }
    return remember(storeFactoryClass) {
        factory
            .invoke(storeFactory)
            .also { it.start() }
    }
}

/**
 * Stable accessor for [Store.accept].
 *
 * [Store] and the classes that implement it are unstable, and passing callbacks with them to
 * lower-level composables will lead to unnecessary recomposition.
 *
 * Usage:
 * ```kotlin
 * val accept = store.rememberAcceptAction()
 *
 * SomeView(
 *     someCallback = { accept(Ui.Some.Event) },
 * )
 * ```
 *
 * @see [rememberAcceptAction]
 */
@Stable
interface ElmAcceptAction<Event : Any> {

    operator fun invoke(event: Event)
}

/**
 * Create stable accessor for [Store.accept].
 *
 * Usage:
 * ```kotlin
 * val accept = store.rememberAcceptAction()
 *
 * SomeView(
 *     someCallback = { accept(Ui.Some.Event) },
 * )
 * ```
 *
 * @see [ElmAcceptAction]
 */
@Composable
inline fun <reified Event : Any> Store<Event, *, *>.rememberAcceptAction(): ElmAcceptAction<Event> =
    remember(Event::class) {
        object : ElmAcceptAction<Event> {
            override fun invoke(event: Event) {
                this@rememberAcceptAction.accept(event)
            }
        }
    }
