package kekmech.ru.common_cache.core

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import java.time.Duration

@Deprecated("Use common_persistent_cache instead")
class DelegatingPersistentCacheHandle<T : Any>(
    private val key: String,
    private val valueClass: Class<T>,
    private val lifetime: Duration?,
    private val cache: PersistentCache,
) : CacheHandle<T> {

    override fun set(value: T) = cache.put(key, value)

    override fun get(): Maybe<T> = cache.get(key, valueClass, lifetime)

    override fun getOrError(): Single<T> = cache.getOrError(key, valueClass, lifetime)

    override fun observe(): Observable<T> = cache.observe(key, valueClass, lifetime)

    override fun clear() = cache.remove(key)

    override fun peek(): T? = cache.peek(key, valueClass, lifetime)

    override fun updateIfPresent(newValue: (previousValue: T) -> T) =
        cache.putIfPresent(key, valueClass, lifetime, newValue)

    override fun update(newValue: (previousValue: T?) -> T) =
        cache.put(key, valueClass, lifetime, newValue)

    override fun hasValue(): Boolean = cache.contains(key)
}
