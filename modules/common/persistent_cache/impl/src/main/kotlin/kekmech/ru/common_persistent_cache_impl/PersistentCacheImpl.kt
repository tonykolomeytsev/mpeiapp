package kekmech.ru.common_persistent_cache_impl

import kekmech.ru.common_coroutines_api.CoroutineDispatchers
import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import kekmech.ru.common_persistent_cache_impl.data.PersistentCacheSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

internal class PersistentCacheImpl(
    private val dispatchers: CoroutineDispatchers,
    private val source: PersistentCacheSource,
) : PersistentCache {

    private val globalSharedFlow = MutableSharedFlow<PersistentCache.Entry<*>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    private val globalMutex = Mutex()

    override suspend fun <T : Serializable> save(
        key: PersistentCacheKey,
        value: T,
    ): Result<Unit> =
        withContext(dispatchers.io()) {
            globalSharedFlow.emit(EntryImpl(key, value))
            runCatching {
                globalMutex.withLock {
                    source.outputStream(key)
                        .let(::ObjectOutputStream)
                        .use { it.writeObject(value) }
                }
            }
        }

    override suspend fun <T : Serializable> restore(key: PersistentCacheKey): Result<T> =
        withContext(dispatchers.io()) {
            runCatching {
                globalMutex.withLock {
                    source.inputStream(key)
                        .let(::ObjectInputStream)
                        .use {
                            @Suppress("UNCHECKED_CAST")
                            it.readObject() as T
                        }
                }
            }
        }

    override suspend fun invalidate(key: PersistentCacheKey): Result<Unit> =
        withContext(dispatchers.io()) {
            runCatching {
                globalMutex.withLock {
                    source.delete(key)
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Serializable> observe(
        key: PersistentCacheKey,
    ): Flow<PersistentCache.Entry<T>> {
        CoroutineScope(dispatchers.io()).launch {
            restore<T>(key)
                .onSuccess {
                    globalSharedFlow.emit(EntryImpl(key, it))
                }
        }
        return globalSharedFlow
            .filter { it.key == key }
            .map { it as PersistentCache.Entry<T> }
    }

    class EntryImpl<T : Serializable>(
        override val key: PersistentCacheKey,
        override val value: T,
    ) : PersistentCache.Entry<T>
}
