package kekmech.ru.common_cache.core

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Represents a single entry in cache.
 * For example, it can be a cache for a list of items.
 *
 * Taken from: https://github.com/vivid-money/lazycache
 */
public interface CacheHandle<T : Any> {

    public fun set(value: T)

    public fun get(): Maybe<T>

    public fun getOrError(): Single<T>

    public fun observe(): Observable<T>

    public fun clear()

    public fun peek(): T?

    public fun updateIfPresent(newValue: (previousValue: T) -> T)

    public fun update(newValue: (previousValue: T?) -> T)

    public fun hasValue(): Boolean
}
