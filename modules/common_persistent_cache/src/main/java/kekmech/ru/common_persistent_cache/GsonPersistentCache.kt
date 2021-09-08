package kekmech.ru.common_persistent_cache

import android.content.Context
import com.google.gson.Gson

@Deprecated("Deprecated in MpeiX v1.9.0")
abstract class GsonPersistentCache<K, V>(
    context: Context,
    cacheName: String,
    private val gson: Gson,
    private val valueClass: Class<V>,
    persistentCacheInteractor: PersistentCacheInteractor = PersistentCacheInteractor()
) : PersistentCache<K, V>(context, cacheName, persistentCacheInteractor) {

    final override fun serializeValue(value: V?): ByteArray? {
        value ?: return null
        return gson.toJson(value).toByteArray()
    }

    final override fun deserializeValue(value: ByteArray): V? = try {
        gson.fromJson(String(value), valueClass)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}