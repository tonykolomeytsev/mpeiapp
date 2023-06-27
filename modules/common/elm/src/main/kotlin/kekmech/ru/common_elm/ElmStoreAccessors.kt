package kekmech.ru.common_elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import org.koin.compose.rememberKoinInject
import vivid.money.elmslie.core.store.Store

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
 * fun MyScreen(
 *     arg1: String,
 *     arg2: Long,
 *     store: ElmStore<...> = rememberElmStore<MyStoreFactory, _, _, _> { create(arg1, arg2) },
 * ) {
 *     ...
 * }
 * ```
 */
@Composable
inline fun <reified Factory : Any, Event : Any, Effect : Any, State : Any> rememberElmStore(
    crossinline factory: Factory.() -> Store<Event, Effect, State>,
): Store<Event, Effect, State> {
    val storeFactoryInstance = rememberKoinInject<Factory>()
    return remember(Factory::class) { factory.invoke(storeFactoryInstance) }
}

/**
 * Stable accessor for [Store.accept].
 *
 * [Store] and the classes that implement it are unstable, and passing callbacks with them to
 * lower-level composables will lead to unnecessary recomposition.
 *
 * Usage:
 * ```kotlin
 * val acceptAction = store.rememberAcceptAction()
 *
 * SomeView(
 *     someCallback = { acceptAction(Ui.Some.Event) },
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
 * val acceptAction = store.rememberAcceptAction()
 *
 * SomeView(
 *     someCallback = { acceptAction(Ui.Some.Event) },
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

/**
 * Collect ELM state as Compose [State].
 */
@Composable
inline fun <reified ElmState : Any> Store<*, *, ElmState>.asState(): State<ElmState> =
    states().collectAsState(initial = currentState)
