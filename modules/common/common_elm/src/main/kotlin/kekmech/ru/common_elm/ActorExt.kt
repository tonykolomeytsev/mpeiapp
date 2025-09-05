package kekmech.ru.common_elm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public fun <T : Any> actorFlow(block: suspend CoroutineScope.() -> T?): Flow<T> = flow {
    coroutineScope { block.invoke(this)?.let { emit(it) } }
}

public fun <T : Any> actorEmptyFlow(block: suspend CoroutineScope.() -> Unit): Flow<T> = flow {
    coroutineScope { block.invoke(this) }
}
