package kekmech.ru.lib_persistent_cache.api

import kotlinx.coroutines.flow.Flow

/**
 * Interface shortening access to [PersistentCache].
 *
 * @see [of]
 * @see [ofList]
 */
public interface PersistentCacheHandle<T> {

    /**
     * Store value in persistent cache.
     *
     * Shortened version of [PersistentCache.save]
     */
    public suspend fun put(value: T): Result<Unit>

    /**
     * Restore value from persistent cache
     *
     * Shortened version of [PersistentCache.restore]
     */
    public suspend fun get(): Result<T>

    /**
     * Delete value from persistent cache
     *
     * Shortened version of [PersistentCache.invalidate]
     */
    public suspend fun clear(): Result<Unit>

    /**
     * Subscribe to changes of this value in the persistent cache.
     *
     * Shortened version of [PersistentCache.observe]
     */
    public fun observe(): Flow<T>
}
