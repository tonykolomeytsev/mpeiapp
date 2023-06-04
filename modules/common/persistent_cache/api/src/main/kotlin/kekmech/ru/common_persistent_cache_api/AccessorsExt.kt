package kekmech.ru.common_persistent_cache_api

import kekmech.ru.common_persistent_cache_api.handle.PersistentCacheHandle
import kekmech.ru.common_persistent_cache_api.handle.PersistentCacheHandleImpl
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty

inline fun <reified T : Serializable> PersistentCache.of(
    key: String? = null,
): ReadOnlyProperty<Any, PersistentCacheHandle<T>> =
    PersistentCacheHandleProperty { _, property ->
        PersistentCacheHandleImpl(
            persistentCache = this@of,
            key = PersistentCacheKey.from(
                prefix = key,
                property = property,
                valueClass = T::class.java,
            ),
            fromSerializable = { it },
            toSerializable = { it },
        )
    }

inline fun <reified T : Serializable> PersistentCache.ofList(
    key: String? = null,
): ReadOnlyProperty<Any, PersistentCacheHandle<List<T>>> =
    PersistentCacheHandleProperty { _, property ->
        PersistentCacheHandleImpl(
            persistentCache = this@ofList,
            key = PersistentCacheKey.from(
                prefix = key,
                property = property,
                valueClass = ArrayList::class.java,
            ),
            fromSerializable = { it },
            toSerializable = {
                when (it) {
                    is ArrayList<T> -> it
                    else -> ArrayList(it)
                }
            },
        )
    }
