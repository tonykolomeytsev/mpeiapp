package kekmech.ru.lib_persistent_cache.impl

import kekmech.ru.lib_coroutines.CoroutineDispatchers
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
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

    // Only the new values of all keys. There is no replay: a single replay slot is shared
    // by all keys, so a save of one key would drop the not yet collected value of another.
    // The saved value is read from the disk for each subscriber instead, see observe()
    private val updates = MutableSharedFlow<PersistentCache.Entry<*>>(
        extraBufferCapacity = UPDATES_BUFFER_CAPACITY,
    )
    private val globalMutex = Mutex()

    override suspend fun <T : Serializable> save(
        key: PersistentCacheKey,
        value: T,
    ): Result<Unit> =
        withContext(dispatchers.io()) {
            val result = runCatching {
                globalMutex.withLock {
                    source.outputStream(key)
                        .let(::ObjectOutputStream)
                        .use { it.writeObject(value) }
                }
            }
            // Emitted after the write, so a subscriber either reads the new value from the disk
            // or receives it here. Emitted even if the write failed, to show the newest value
            updates.emit(EntryImpl(key, value))
            result
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
    ): Flow<PersistentCache.Entry<T>> =
        updates
            // Runs after the subscription to updates, so the values saved during the restore
            // are collected after the restored one and not lost
            .onSubscription {
                restore<T>(key).onSuccess { emit(EntryImpl(key, it)) }
            }
            .filter { it.key == key }
            .map { it as PersistentCache.Entry<T> }

    private companion object {

        const val UPDATES_BUFFER_CAPACITY = 64
    }

    class EntryImpl<T : Serializable>(
        override val key: PersistentCacheKey,
        override val value: T,
    ) : PersistentCache.Entry<T>
}
