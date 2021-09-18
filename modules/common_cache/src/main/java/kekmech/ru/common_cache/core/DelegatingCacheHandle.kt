package kekmech.ru.common_cache.core

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.in_memory_cache.InMemoryCache

/**
 * Taken from: https://github.com/vivid-money/lazycache
 */
internal class DelegatingCacheHandle<T : Any>(
    private val key: String,
    private val cache: InMemoryCache
) : CacheHandle<T> {

    override fun set(value: T) = cache.put(key, value)

    override fun get(): Maybe<T> = cache.get(key)

    override fun getOrError(): Single<T> = cache.getOrError(key)

    override fun observe(): Observable<T> = cache.observe(key)

    override fun clear() = cache.remove(key)

    override fun peek(): T? = cache.peek(key)

    override fun updateIfPresent(newValue: (previousValue: T) -> T) =
        cache.putIfPresent(key, newValue)

    override fun update(newValue: (previousValue: T?) -> T) = cache.put(key, newValue)

    override fun hasValue(): Boolean = cache.contains(key)
}