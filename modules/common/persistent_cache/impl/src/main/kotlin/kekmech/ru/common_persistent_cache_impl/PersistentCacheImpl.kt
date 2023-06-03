package kekmech.ru.common_persistent_cache_impl

import kekmech.ru.common_coroutines_api.CoroutineDispatchers
import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import kekmech.ru.common_persistent_cache_impl.data.PersistentCacheSource
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

    private val globalMutex = Mutex()

    override suspend fun <T : Serializable> save(key: PersistentCacheKey, value: T): Result<Unit> =
        withContext(dispatchers.io()) {
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
}
