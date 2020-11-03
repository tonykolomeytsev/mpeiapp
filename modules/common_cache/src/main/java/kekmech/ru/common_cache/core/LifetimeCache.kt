package kekmech.ru.common_cache.core

import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.subjects.BehaviorSubject
import kekmech.ru.common_kotlin.Option
import java.util.concurrent.ConcurrentHashMap

class LifetimeCache {

    private val cache = ConcurrentHashMap<String, BehaviorSubject<Option<Any>>>()

    /**
     * Puts value into cache
     */
    fun <T : Any> put(key: String, value: T) = getSubject<T>(key).onNext(Option(value))

    /**
     * Get previous value, transform it and put it into cache
     */
    fun <T : Any> put(key: String, transform: (previous: T?) -> T?) {
        getSubject<T>(key).let { s ->
            synchronized(s) {
                transform(s.value?.value)?.let{ s.onNext(Option(it)) }
            }
        }
    }

    /**
     * Removes value from cache
     */
    fun remove(key: String) {
        cache[key]?.onNext(Option(null))
    }

    /**
     * Synchronously retrieves the current value from the cache, it can be equal to null
     */
    fun <T : Any> getIfPresent(key: String): T? = getSubject<T>(key).value?.value

    /**
     * Return maybe of cached value
     */
    fun <T : Any> asMaybe(key: String): Maybe<T> = Maybe.create<T> { emitter: MaybeEmitter<T> ->
        getIfPresent<T>(key)?.let { emitter.onSuccess(it) } ?: emitter.onComplete()
    }.observeOn(io())

    /**
     * Return observable of cached value
     */
    fun <T : Any> asObservable(key: String): Observable<T> = getSubject<T>(key)
        .hide()
        .observeOn(io())
        .filter { it.isNotEmpty }
        .map { it.value }

    fun contains(key: String): Boolean = getSubject<Any>(key).value?.isNotEmpty ?: false

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getSubject(key: String): BehaviorSubject<Option<T>> =
        cache.getOrPut(key) { BehaviorSubject.create() } as BehaviorSubject<Option<T>>
}