package kekmech.ru.common_persistent_cache

import io.reactivex.rxjava3.core.Single

@Deprecated("Deprecated in MpeiX v1.9.0")
fun <K, V : Any> Single<V>.orFromPersistentCache(key: K, persistentCache: PersistentCache<K, V>): Single<V> = this
    .doOnSuccess { persistentCache.put(key, it) }
    .onErrorResumeNext { Single.just(persistentCache.get(key)!!) }


