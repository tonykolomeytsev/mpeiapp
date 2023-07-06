package kekmech.ru.lib_persistent_cache.impl

import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.PersistentCacheHandle
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.Serializable

@PublishedApi
internal class PersistentCacheHandleImpl<S : Serializable, D>(
    private val persistentCache: PersistentCache,
    private val key: PersistentCacheKey,
    private val fromSerializable: (S) -> D,
    private val toSerializable: (D) -> S,
) : PersistentCacheHandle<D> {

    override suspend fun put(value: D): Result<Unit> =
        persistentCache.save(
            key = key,
            value = toSerializable.invoke(value),
        )

    override suspend fun get(): Result<D> =
        persistentCache.restore<S>(key).map(fromSerializable)

    override suspend fun clear(): Result<Unit> =
        persistentCache.invalidate(key)

    override fun observe(): Flow<D> =
        persistentCache
            .observe<S>(key)
            .map { fromSerializable.invoke(it.value) }
}
