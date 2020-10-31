package kekmech.ru.common_persistent_cache

import io.reactivex.Single

fun <K, V> Single<V>.orFromPersistentCache(key: K, persistentCache: PersistentCache<K, V>): Single<V> = this
    .doOnSuccess { persistentCache.put(key, it) }
    .onErrorResumeNext { Single.just(persistentCache.get(key)) }


