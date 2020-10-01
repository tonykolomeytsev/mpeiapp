package kekmech.ru.common_persistent_cache

import android.content.Context
import java.io.File

abstract class PersistentCache<K, V>(
    context: Context,
    cacheName: String,
    private val persistentCacheInteractor: PersistentCacheInteractor = PersistentCacheInteractor()
) {
    private val cacheDir = File(context.filesDir, cacheName).apply { mkdirs() }

    fun put(key: K, value: V?) {
        println("PersistentCache: PUT key=$key, value=$value")
        val stringKey = generateKeyForPut(key, value) ?: return
        val serializedValue = serializeValue(value)
        serializedValue?.let { byteArray ->
            val file = File(cacheDir, stringKey)
            if (!file.exists()) file.createNewFile()
            persistentCacheInteractor.writeBytes(file, byteArray)
        }
    }

    fun get(key: K): V? {
        println("PersistentCache: GET key=$key")
        val stringKey = generateKeyForGet(key) ?: return null
        val file = File(cacheDir, stringKey)
        if (!file.exists()) return null
        val serializedValue = persistentCacheInteractor.readBytes(file)
        return deserializeValue(serializedValue)
    }

    abstract fun generateKeyForPut(key: K, value: V?): String?

    abstract fun generateKeyForGet(key: K): String?

    abstract fun serializeValue(value: V?): ByteArray?

    abstract fun deserializeValue(value: ByteArray): V?
}