package kekmech.ru.common_cache.persistent_cache

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.core.CacheHandle
import java.time.Duration

fun <T : Any> PersistentCache.ofComposited(
    key: String,
    valueClass: Class<T>,
    lifetime: Duration,
): CacheHandle<T> =
    object : CompositedCacheHandle<T> {

        override val foreverLifetimeCacheHandle = of(key, valueClass, null)
        override val limitedLifetimeCacheHandle = of(key, valueClass, lifetime)

        override fun set(value: T) =
            limitedLifetimeCacheHandle.set(value)

        override fun get(): Maybe<T> =
            limitedLifetimeCacheHandle.get()

        override fun observe(): Observable<T> =
            limitedLifetimeCacheHandle.observe()

        override fun clear() =
            limitedLifetimeCacheHandle.clear()

        override fun peek(): T? =
            limitedLifetimeCacheHandle.peek()

        override fun updateIfPresent(newValue: (previousValue: T) -> T) =
            limitedLifetimeCacheHandle.updateIfPresent(newValue)

        override fun update(newValue: (previousValue: T?) -> T) =
            limitedLifetimeCacheHandle.update(newValue)

        override fun hasValue(): Boolean =
            limitedLifetimeCacheHandle.hasValue()
    }

private interface CompositedCacheHandle<T : Any> : CacheHandle<T> {

    val foreverLifetimeCacheHandle: CacheHandle<T>
    val limitedLifetimeCacheHandle: CacheHandle<T>
}

fun <T : Any> CacheHandle<T>.getOrLoadFrom(singleSource: Single<T>): Single<T> {
    check(this is CompositedCacheHandle) {
        "getOrLoadFrom(Single<T>) available only for Composited cache handles"
    }
    return limitedLifetimeCacheHandle
        .get()
        .switchIfEmpty(singleSource)
        .doOnSuccess(limitedLifetimeCacheHandle::set)
        .onErrorResumeWith(foreverLifetimeCacheHandle.get().toSingle())
}