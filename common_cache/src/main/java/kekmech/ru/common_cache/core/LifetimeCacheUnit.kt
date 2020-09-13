package kekmech.ru.common_cache.core

import io.reactivex.Maybe
import io.reactivex.Observable

interface LifetimeCacheUnit<T : Any> {

    fun set(value: T)
    fun asMaybe(): Maybe<T>
    fun asObservable(): Observable<T>
    fun clear()
    fun getIfPresent(): T?
    fun update(transform: (previous: T?) -> T?)
    fun isNotEmpty(): Boolean
}