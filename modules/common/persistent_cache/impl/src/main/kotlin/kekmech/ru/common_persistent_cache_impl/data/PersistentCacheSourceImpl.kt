package kekmech.ru.common_persistent_cache_impl.data

import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import kekmech.ru.common_persistent_cache_impl.PersistentCacheDir
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal class PersistentCacheSourceImpl(
    private val cacheDir: PersistentCacheDir,
) : PersistentCacheSource {

    override fun inputStream(key: PersistentCacheKey): InputStream =
        File(cacheDir.dir, key.filename())
            .inputStream()
            .buffered()

    override fun outputStream(key: PersistentCacheKey): OutputStream =
        File(cacheDir.dir, key.filename())
            .outputStream()
            .buffered()

    override fun delete(key: PersistentCacheKey) {
        File(cacheDir.dir, key.filename()).delete()
    }
}
