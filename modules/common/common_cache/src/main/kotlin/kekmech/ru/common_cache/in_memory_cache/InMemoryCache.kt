package kekmech.ru.common_cache.in_memory_cache

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kekmech.ru.common_cache.core.CacheHandle
import kekmech.ru.common_cache.core.CacheIsEmptyException
import kekmech.ru.common_cache.core.DelegatingCacheHandle
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Taken from: https://github.com/vivid-money/lazycache
 */
@Suppress("TooManyFunctions")
public class InMemoryCache(
    private val gson: Gson,
) {

    private val keysToBeCleared = Collections.newSetFromMap(ConcurrentHashMap<String, Boolean>())
    private val cache = ConcurrentHashMap<String, BehaviorSubject<Optional<Any>>>()

    public fun <T : Any> put(key: String, value: T) {
        getSubject<T>(key).onNext(Optional.of(value))
    }

    public fun remove(key: String) {
        cache[key]?.onNext(Optional.empty())
    }

    public fun <T : Any> peek(key: String): T? =
        getSubject<T>(key).value
            ?.orElse(null)


    public fun <T : Any> putIfPresent(
        key: String,
        newValue: (previousValue: T) -> T,
    ): Unit = updateInternal<T>(key) { oldValue ->
        oldValue?.let { newValue.invoke(it) }
    }

    public fun <T : Any> put(
        key: String,
        newValue: (previousValue: T?) -> T,
    ): Unit = updateInternal(key, newValue)

    public fun <T : Any> get(key: String): Maybe<T> =
        Maybe
            .create<T> { emitter ->
                getSubject<T>(key).value
                    ?.orElse(null)
                    ?.let(emitter::onSuccess)
                    ?: emitter.onComplete()
            }
            .observeOn(io())

    public fun <T : Any> getOrError(key: String): Single<T> =
        Single
            .create<T> { emitter ->
                getSubject<T>(key).value
                    ?.orElse(null)
                    ?.let(emitter::onSuccess)
                    ?: emitter.onError(CacheIsEmptyException(key))
            }
            .observeOn(io())


    public fun <T : Any> observe(key: String): Observable<T> =
        getSubject<T>(key)
            .hide()
            .observeOn(io())
            .mapOptional { it }

    public fun contains(key: String): Boolean =
        getSubject<Any>(key).value
            ?.isPresent
            ?: false

    public fun <T : Any> of(
        key: String,
        keepAlways: Boolean,
    ): CacheHandle<T> {
        if (!keepAlways) keysToBeCleared.add(key)
        return DelegatingCacheHandle(key, this)
    }

    /**
     * Clears data in cache and all previously returned [CacheHandle]s, except then ones created with keepAlways = true
     */
    public fun clear() {
        keysToBeCleared.forEach { remove(it) }
    }

    private fun <T : Any> updateInternal(key: String, newValue: (previousValue: T?) -> T?) {
        val subject = getSubject<T>(key)
        synchronized(subject) {
            newValue(
                subject.value?.orElse(null)
            )?.let {
                subject.onNext(Optional.of(it))
            }
        }
    }

    private fun <T : Any> getSubject(key: String): BehaviorSubject<Optional<T>> {
        @Suppress("UNCHECKED_CAST")
        return cache.getOrPut(key) { BehaviorSubject.create() } as BehaviorSubject<Optional<T>>
    }
}
