package kekmech.ru.common_persistent_cache_api

import kotlinx.coroutines.flow.Flow
import java.io.Serializable

interface PersistentCache {

    suspend fun <T : Serializable> save(key: PersistentCacheKey, value: T): Result<Unit>

    suspend fun <T : Serializable> restore(key: PersistentCacheKey): Result<T>

    suspend fun invalidate(key: PersistentCacheKey): Result<Unit>

    fun <T : Serializable> observe(key: PersistentCacheKey): Flow<Entry<T>>

    data class Entry<T : Serializable>(val key: PersistentCacheKey, val value: T)
}
