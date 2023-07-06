package kekmech.ru.lib_persistent_cache.impl.data

import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
import java.io.InputStream
import java.io.OutputStream

internal interface PersistentCacheSource {

    fun inputStream(key: PersistentCacheKey): InputStream

    fun outputStream(key: PersistentCacheKey): OutputStream

    fun delete(key: PersistentCacheKey)
}
