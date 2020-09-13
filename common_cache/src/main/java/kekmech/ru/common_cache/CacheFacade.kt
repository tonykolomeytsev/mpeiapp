package kekmech.ru.common_cache

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.io
import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_cache.core.LifetimeCacheUnit
import kekmech.ru.common_cache.core.LifetimeCacheUnitImpl
import kekmech.ru.common_kotlin.getOrPut
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class CacheFacade<T : Any>(
    private val cache: LifetimeCacheUnit<T>,
    private val dataSource: () -> Observable<T>
) {
    private val atomicDataSource = AtomicReference<Observable<T>>()

    fun getIfPresent(): T? = cache.getIfPresent()

    /**
     * Returns single value, update data if necessary
     */
    fun asSingle(): Single<T> = asObservable().firstOrError()

    /**
     * Returns data observer, update data if necessary
     */
    fun asObservable(): Observable<T> = Observable
        .merge(ensureFirstValue().toObservable(), cache.asObservable())
        .distinctUntilChanged()

    fun updateIfEmpty(): Completable = asSingle().ignoreElement()

    /**
     * Reloads data in cache and start observing the [dataSource]
     * NOTE: Don't use for cache of [Single]. Use [forceUpdateAndSingle] instead
     */
    fun forceUpdateAndObserve(): Observable<T> = getDataSource()

    /**
     * Reloads data in cache and returns the first value provided by this [dataSource]
     */
    fun forceUpdateAndSingle(): Single<T> = forceUpdateAndObserve().firstOrError()

    fun forceUpdate(): Completable = forceUpdateAndObserve().ignoreElements()

    fun set(value: T) = cache.set(value)

    fun update(transform: (previous: T?) -> T?) = cache.update(transform)

    fun clear() = cache.clear()

    fun isNotEmpty() = cache.isNotEmpty()

    private fun ensureFirstValue(): Single<T> = cache.asMaybe().switchIfEmpty(getDataSource().firstOrError())

    private fun getDataSource(): Observable<T> = atomicDataSource.getOrPut {
        dataSource()
            .subscribeOn(io())
            .doOnNext { cache.set(it) }
            .share()
            .doFinally { atomicDataSource.set(null) }
    }
}

fun <T : Any> LifetimeCache.create(dataSource: () -> Single<T>): CacheFacade<T> =
    CacheFacade(
        LifetimeCacheUnitImpl(UUID.randomUUID().toString(), this)
    ) {dataSource().toObservable()}