package kekmech.ru.lib_persistent_cache.impl.data

import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
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

    override fun outputStream(key: PersistentCacheKey): OutputStream {
        // The directory lives in Context.cacheDir: it doesn't exist on fresh installs and the
        // system or the user ("Clear cache") can delete it at any time, so create it on each write
        cacheDir.dir.mkdirs()
        return File(cacheDir.dir, key.filename())
            .outputStream()
            .buffered()
    }

    override fun delete(key: PersistentCacheKey) {
        File(cacheDir.dir, key.filename()).delete()
    }
}
