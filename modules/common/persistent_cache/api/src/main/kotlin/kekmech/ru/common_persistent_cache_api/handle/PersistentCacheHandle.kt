package kekmech.ru.common_persistent_cache_api.handle

import kotlinx.coroutines.flow.Flow

/**
 * Interface shortening access to
 * [PersistentCache][kekmech.ru.common_persistent_cache_api.PersistentCache].
 *
 * @see [kekmech.ru.common_persistent_cache_api.of]
 * @see [kekmech.ru.common_persistent_cache_api.ofList]
 */
interface PersistentCacheHandle<T> {

    /**
     * Store value in persistent cache.
     *
     * Shortened version of [kekmech.ru.common_persistent_cache_api.PersistentCache.save]
     */
    suspend fun put(value: T): Result<Unit>

    /**
     * Restore value from persistent cache
     *
     * Shortened version of [kekmech.ru.common_persistent_cache_api.PersistentCache.restore]
     */
    suspend fun get(): Result<T>

    /**
     * Delete value from persistent cache
     *
     * Shortened version of [kekmech.ru.common_persistent_cache_api.PersistentCache.invalidate]
     */
    suspend fun clear(): Result<Unit>

    /**
     * Subscribe to changes of this value in the persistent cache.
     *
     * Shortened version of [kekmech.ru.common_persistent_cache_api.PersistentCache.observe]
     */
    fun observe(): Flow<T>
}
