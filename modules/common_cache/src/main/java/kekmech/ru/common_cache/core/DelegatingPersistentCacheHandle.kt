package kekmech.ru.common_cache.core

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_cache.persistent_cache.PersistentCache

class DelegatingPersistentCacheHandle<T : Any>(
    private val key: String,
    private val valueClass: Class<T>,
    private val cache: PersistentCache,
) : CacheHandle<T> {

    override fun set(value: T) = cache.put(key, value)

    override fun get(): Maybe<T> = cache.get(key, valueClass)

    override fun observe(): Observable<T> = cache.observe(key, valueClass)

    override fun clear() = cache.remove(key)

    override fun peek(): T? = cache.peek(key, valueClass)

    override fun updateIfPresent(newValue: (previousValue: T) -> T) =
        cache.putIfPresent(key, valueClass, newValue)

    override fun update(newValue: (previousValue: T?) -> T) = cache.put(key, newValue)

    override fun hasValue(): Boolean = cache.contains(key)
}