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
interface CacheHandle<T : Any> {

    fun set(value: T)

    fun get(): Maybe<T>

    fun getOrError(): Single<T>

    fun observe(): Observable<T>

    fun clear()

    fun peek(): T?

    fun updateIfPresent(newValue: (previousValue: T) -> T)

    fun update(newValue: (previousValue: T?) -> T)

    fun hasValue(): Boolean
}