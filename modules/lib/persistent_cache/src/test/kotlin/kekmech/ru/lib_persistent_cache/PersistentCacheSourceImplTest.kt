package kekmech.ru.lib_persistent_cache

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheDir
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheSourceImpl
import java.io.File
import java.nio.file.Files

internal class PersistentCacheSourceImplTest : StringSpec({

    "Write creates missing cache directory" {
        val root = Files.createTempDirectory("persistent-cache-test").toFile()
        try {
            val source = PersistentCacheSourceImpl(PersistentCacheDir(File(root, "persistent")))
            val key = PersistentCacheKey.from(name = "key", valueClass = String::class.java)

            source.outputStream(key).use { it.write(byteArrayOf(1, 2, 3)) }
            val bytes = source.inputStream(key).use { it.readBytes() }

            bytes.toList() shouldContainExactly listOf<Byte>(1, 2, 3)
        } finally {
            root.deleteRecursively()
        }
    }
})
