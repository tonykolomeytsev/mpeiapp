package kekmech.ru.common_cache.core

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

@Deprecated("Deprecated in MpeiX v1.9.0")
interface LifetimeCacheUnit<T : Any> {

    fun set(value: T)
    fun asMaybe(): Maybe<T>
    fun asObservable(): Observable<T>
    fun clear()
    fun getIfPresent(): T?
    fun update(transform: (previous: T?) -> T?)
    fun isNotEmpty(): Boolean
}