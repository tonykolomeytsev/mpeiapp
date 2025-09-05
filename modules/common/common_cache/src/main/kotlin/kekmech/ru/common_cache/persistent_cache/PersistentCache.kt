package kekmech.ru.common_cache.persistent_cache

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kekmech.ru.common_cache.core.CacheHandle
import kekmech.ru.common_cache.core.CacheIsEmptyException
import kekmech.ru.common_cache.core.DelegatingPersistentCacheHandle
import java.io.File
import java.time.Duration
import java.util.*

@Suppress("TooManyFunctions")
public open class PersistentCache(
    private val gson: Gson,
    private val cacheDirectory: File,
) {

    private val commonSubject = BehaviorSubject.create<PipelineEntry>()
    private val cache = Collections.synchronizedMap(
        WeakHashMap<String, Any>(MEMORY_CACHE_CAPACITY)
    )

    public fun <T : Any> put(key: String, value: T) {
        updatePersistentInternal(key, value)
        cache[key] = value
        commonSubject.onNext(PipelineEntry(key, Optional.of(value)))
    }

    public fun remove(key: String) {
        removePersistentInternal(key)
        cache.remove(key)
        commonSubject.onNext(PipelineEntry(key, Optional.empty()))
    }

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> peek(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
        autoUpdateSubject: Boolean = true,
    ): T? {
        return cache[key] as T?
            ?: peekPersistent(key, valueClass, lifetime)
                ?.also {
                    if (autoUpdateSubject) {
                        cache[key] = it
                        commonSubject.onNext(PipelineEntry(key, Optional.of(it)))
                    }
                }
    }

    public fun <T : Any> putIfPresent(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
        modifier: (previousValue: T) -> T,
    ) {
        val previousValue = peek(key, valueClass, lifetime, autoUpdateSubject = false)
        previousValue?.let { put(key, modifier.invoke(it)) }
    }

    public fun <T : Any> put(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
        modifier: (previousValue: T?) -> T,
    ) {
        val previousValue = peek(key, valueClass, lifetime, autoUpdateSubject = false)
        put(key, modifier.invoke(previousValue))
    }

    public fun <T : Any> get(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
    ): Maybe<T> =
        Maybe
            .create<T> { emitter ->
                peek(key, valueClass, lifetime)
                    ?.let(emitter::onSuccess)
                    ?: emitter.onComplete()
            }
            .observeOn(io())

    public fun <T : Any> getOrError(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
    ): Single<T> =
        Single
            .create<T> { emitter ->
                peek(key, valueClass, lifetime)
                    ?.let(emitter::onSuccess)
                    ?: emitter.onError(CacheIsEmptyException(key))
            }
            .observeOn(io())

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> observe(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
    ): Observable<T> {
        peek(key, valueClass, lifetime)
        return commonSubject
            .filter { it.key == key }
            .observeOn(io())
            .mapOptional { (_, optional) -> optional.map { it as T } }
    }

    public fun contains(key: String): Boolean {
        val storedInMemory = cache.containsKey(key)
        val storedInCacheDir =
            File(cacheDirectory, key).let { it.exists() && it.isFile }
        return storedInMemory or storedInCacheDir
    }

    public fun <T : Any> of(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration? = null,
    ): CacheHandle<T> =
        DelegatingPersistentCacheHandle(key, valueClass, lifetime, this)

    public fun clear() {
        cache.clear()
        cacheDirectory.listFiles()?.forEach { it.delete() }
        commonSubject.onNext(PipelineEntry("", Optional.empty()))
    }

    private fun updatePersistentInternal(key: String, newValue: Any) {
        gson.toJson(newValue).toByteArray().let { byteArray ->
            val file = File(cacheDirectory, key)
            if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
            if (!file.exists()) file.createNewFile()
            file.writeBytes(byteArray)
            file.setLastModified(System.currentTimeMillis())
        }
    }

    private fun removePersistentInternal(key: String) {
        val file = File(cacheDirectory, key)
        if (file.exists() && file.isFile) {
            file.delete()
        }
    }

    private fun <T : Any> peekPersistent(
        key: String,
        valueClass: Class<T>,
        lifetime: Duration?,
    ): T? {
        val file = File(cacheDirectory, key)

        // check cache lifetime expiration
        if (lifetime != null) {
            val expirationTimestamp = file.lastModified() + lifetime.toMillis()
            if (expirationTimestamp < System.currentTimeMillis()) {
                // we don't need to delete file from cache dir
                // just return null
                return null
            }
        }
        if (file.exists()) {
            return runCatching {
                gson.fromJson(String(file.readBytes()), valueClass)
            }.getOrNull()
        }
        return null
    }

    private data class PipelineEntry(val key: String, val value: Optional<Any>)

    private companion object {

        const val MEMORY_CACHE_CAPACITY = 16
    }
}
