package kekmech.ru.lib_persistent_cache.api

import kotlinx.coroutines.flow.Flow

/**
 * Interface shortening access to [PersistentCache].
 *
 * @see [of]
 * @see [ofList]
 */
interface PersistentCacheHandle<T> {

    /**
     * Store value in persistent cache.
     *
     * Shortened version of [PersistentCache.save]
     */
    suspend fun put(value: T): Result<Unit>

    /**
     * Restore value from persistent cache
     *
     * Shortened version of [PersistentCache.restore]
     */
    suspend fun get(): Result<T>

    /**
     * Delete value from persistent cache
     *
     * Shortened version of [PersistentCache.invalidate]
     */
    suspend fun clear(): Result<Unit>

    /**
     * Subscribe to changes of this value in the persistent cache.
     *
     * Shortened version of [PersistentCache.observe]
     */
    fun observe(): Flow<T>
}
