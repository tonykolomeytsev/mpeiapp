package kekmech.ru.lib_persistent_cache.api

import kotlinx.coroutines.flow.Flow
import java.io.Serializable

/**
 * # PersistentCache
 *
 * A persistent cache that serializes objects to and from an array of bytes using
 * standard JVM tools. All serializable objects must be marked with the [Serializable] interface.
 *
 * ## Usage
 *
 * With accessors:
 *
 * ```kotlin
 * val persistentCache: PersistentCache // injected
 * ...
 * // `of` accessor for simple classes
 * val myValueCache by persistentCache.of<MyValueClass>()
 *
 * // `ofList` accessor for lists
 * val myValuesList by persistentCache.ofList<MyValueClass>()
 * ```
 *
 * With direct access to `PersistentCache`:
 *
 * ```kotlin
 * val persistentCache: PersistentCache // injected
 * ...
 * persistentCache.save(
 *     key = PersistentCacheKey.from(...),
 *     value = myValue,
 * )
 *
 * persistentCache.restore(
 *     key = PersistentCacheKey.from(...),
 * )
 * ```
 *
 * @see PersistentCacheKey
 * @see of
 * @see ofList
 */
public interface PersistentCache {

    /**
     * Store value in persistent cache.
     */
    public suspend fun <T : Serializable> save(key: PersistentCacheKey, value: T): Result<Unit>

    /**
     * Restore value from persistent cache
     */
    public suspend fun <T : Serializable> restore(key: PersistentCacheKey): Result<T>

    /**
     * Delete value from persistent cache
     */
    public suspend fun invalidate(key: PersistentCacheKey): Result<Unit>

    /**
     * Subscribe to changes to a specific key in the persistent cache.
     *
     * If there is a value in the cache for the given key, each time you subscribe
     * to that key, the value will be emitted. Also, the value will be emitted when a new
     * value for this key is stored in the cache.
     */
    public fun <T : Serializable> observe(key: PersistentCacheKey): Flow<Entry<T>>

    public interface Entry<T : Serializable> {

        public val key: PersistentCacheKey
        public val value: T
    }
}
