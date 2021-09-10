package kekmech.ru.common_cache.core.lazy_cache

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kekmech.ru.common_cache.core.in_memory_cache.CacheHandle
import java.util.concurrent.atomic.AtomicReference

/**
 * Represents a cache of a single request with specific arguments.
 *
 * Taken from: https://github.com/vivid-money/lazycache
 */
@Suppress("TooManyFunctions", "Unused", "MemberVisibilityCanBePrivate")
class LazyCache<T : Any, V>(
    private val cache: CacheHandle<T>,
    val args: V,
    private val updater: (args: V) -> Observable<T>,
) {

    private val sharedUpdater = AtomicReference<Observable<T>>()

    /**
     * Returns a single value
     * Fetches data if necessary
     * NOTE: [forceUpdateAndObserve] errors will not be propagated to this observable
     */
    fun get(): Single<T> = observe().firstOrError()

    /**
     * Returns current value in cache or `null`
     */
    fun peek(): T? = cache.peek()

    /**
     * Allows to observe data
     * Fetches data if necessary
     * NOTE: [forceUpdateAndObserve] errors will not be propagated to this observable
     */
    fun observe(): Observable<T> = Observable // Required to be able to handle cache clearing
        .merge(ensureFirstValue().toObservable(), cache.observe())
        .distinctUntilChanged()

    /**
     * Ensures data is loaded
     */
    fun updateIfEmpty(): Completable = get().ignoreElement()

    /**
     * Reloads data in cache and then starts observing the [updater]
     * NOTE: Present and all subsequent values will not be returned
     * NOTE: Don't use for cache of [Single]s. Use [forceUpdateAndGet] instead.
     */
    fun forceUpdateAndObserve(): Observable<T> = getUpdater()

    /**
     * Reloads data in cache and returns the first value provided by this [updater]
     * NOTE: Present and all subsequent values will not be returned
     */
    fun forceUpdateAndGet(): Single<T> = forceUpdateAndObserve().firstOrError()

    /**
     * Reloads data in cache
     */
    fun forceUpdate(): Completable = forceUpdateAndObserve().ignoreElements()

    /**
     * Reloads data in cache ONLY if value was added before
     */
    fun updateIfNeeded(): Completable {
        return Single.fromCallable { hasValue() }
            .filter { it }
            .flatMapCompletable { forceUpdate() }
    }

    /**
     * Updates current value in cache
     * NOTE: This value may be overwritten later by currently running updaters
     */
    fun set(value: T) = cache.set(value)

    /**
     * Modifies data **only** if it's already present in cache
     */
    fun updateIfPresent(updater: (oldValue: T) -> T) = cache.updateIfPresent(updater)

    /**
     * Modifies data in cache. In case cache is empty, `oldValue` will be null
     */
    fun update(updater: (oldValue: T?) -> T) = cache.update(updater)

    /**
     * Clear current cache value
     */
    fun clear() = cache.clear()

    /**
     * Checks whether cache has a value
     */
    fun hasValue(): Boolean = cache.hasValue()

    private fun ensureFirstValue(): Single<T> =
        cache
            .get()
            .switchIfEmpty(getUpdater().firstOrError())

    private fun getUpdater(): Observable<T> = sharedUpdater.getOrPut { shareUpdater() }

    private fun shareUpdater(): Observable<T> =
        updater(args)
            .subscribeOn(io())
            .doOnNext { cache.set(it) }
            .share()
            .doFinally { sharedUpdater.set(null) }

    /**
     * Concurrent getOrPut, that is thread safe.
     *
     * Returns the value. If the value is not present, calls the [defaultValue] function,
     * set its result and returns it.
     *
     * This method guarantees not to put the value if it's already there,
     * but the [defaultValue] function may be invoked even if value is already present.
     */
    private fun <T> AtomicReference<T>.getOrPut(defaultValue: () -> T): T {
        // Implementation is taken from ConcurrentHashMap.getOrPut
        return this.get() ?: defaultValue().also { this.compareAndSet(null, it) }
    }
}