package kekmech.ru.common_cache

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.core.lazy_cache.LazyCache
import kekmech.ru.common_cache.core.lazy_cache.LazyCacheFactory
import kekmech.ru.common_cache.core.lazy_cache.LazyCacheHolder

class NoArgLazyCacheHolder<T : Any>(
    private val holder: LazyCacheHolder<T, Unit>,
) : BaseLazyCacheHolder<T, Unit>(holder) {

    fun get() = holder.getCache(Unit)
}

class OneArgLazyCacheHolder<T : Any, Arg : Any>(
    private val holder: LazyCacheHolder<T, Arg>,
) : BaseLazyCacheHolder<T, Arg>(holder) {

    fun get(arg: Arg) = holder.getCache(arg)
}

class TwoArgsLazyCacheHolder<T : Any, Arg1, Arg2>(
    private val holder: LazyCacheHolder<T, Pair<Arg1, Arg2>>,
) : BaseLazyCacheHolder<T, Pair<Arg1, Arg2>>(holder) {

    fun get(arg1: Arg1, arg2: Arg2) = holder.getCache(arg1 to arg2)
}

open class BaseLazyCacheHolder<T : Any, Arg : Any>(
    private val holder: LazyCacheHolder<T, Arg>,
) {

    fun clear() = holder.clear()

    fun <V> mapAllCached(action: (LazyCache<T, Arg>) -> V): List<V> = holder.getAllCached().map { action(it) }
}

inline fun <reified Result : Any> LazyCacheFactory.fromSingle(
    customKey: String? = null,
    crossinline updater: () -> Single<Result>,
) =
    NoArgLazyCacheHolder(
        cacheHolder(customKey) {
            updater().toObservable()
        }
    )

inline fun <Arg : Any, reified Result : Any> LazyCacheFactory.fromSingle(
    customKey: String? = null,
    crossinline updater: (arg: Arg) -> Single<Result>,
) =
    OneArgLazyCacheHolder(
        cacheHolder(customKey) { arg: Arg ->
            updater(arg).toObservable()
        }
    )

inline fun <Arg1, Arg2, reified Result : Any> LazyCacheFactory.fromSingle(
    customKey: String? = null,
    crossinline updater: (arg1: Arg1, arg2: Arg2) -> Single<Result>,
) =
    TwoArgsLazyCacheHolder(
        cacheHolder(customKey) { args: Pair<Arg1, Arg2> ->
            updater(args.first, args.second).toObservable()
        }
    )
