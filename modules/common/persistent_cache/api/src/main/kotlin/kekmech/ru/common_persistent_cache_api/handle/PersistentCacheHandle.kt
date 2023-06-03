package kekmech.ru.common_persistent_cache_api.handle

import kotlinx.coroutines.flow.Flow

interface PersistentCacheHandle<T> {

    suspend fun put(value: T): Result<Unit>

    suspend fun get(): Result<T>

    suspend fun clear(): Result<Unit>

    fun observe(): Flow<T>
}
