package kekmech.ru.common_persistent_cache_impl.data

import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import java.io.InputStream
import java.io.OutputStream

internal interface PersistentCacheSource {

    fun inputStream(key: PersistentCacheKey): InputStream

    fun outputStream(key: PersistentCacheKey): OutputStream

    fun delete(key: PersistentCacheKey)
}
