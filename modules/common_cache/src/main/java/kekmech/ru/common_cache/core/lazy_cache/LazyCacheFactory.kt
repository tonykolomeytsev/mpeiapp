package kekmech.ru.common_cache.core.lazy_cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_cache.core.in_memory_cache.CacheKeyGenerator
import kekmech.ru.common_cache.core.in_memory_cache.InMemoryCache
import java.util.concurrent.ConcurrentHashMap

/**
 * Factory for caches of a specific updater.
 *
 * Use [of] functions to create instances.
 * Each function takes an `updater` to load data, while will be cached inside [inMemoryCache].
 *
 * Usage example:
 * ```
 * class SomeClass(
 *     private val lazyCacheFactory: LazyCacheFactory,
 *     private val api: SomeApi
 * ) {
 *     private val requestCache = lazyCacheFactory.of(api::someApiMethod)
 *
 *     fun observeData(argument: SomeType) = requestCache.get(argument).observe()
 *
 *     fun forceUpdateData(argument: SomeType) = requestCache.get(argument).forceUpdate()
 * }
 * ```
 *
 * Taken from: https://github.com/vivid-money/lazycache
 */
class LazyCacheFactory(
    val inMemoryCache: InMemoryCache,
    val gson: Gson,
) {

    val holders = ConcurrentHashMap<String, LazyCacheHolder<Any, Any>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <Args : Any, reified Result : Any> cacheHolder(
        customKey: String?,
        noinline updater: (args: Args) -> Observable<Result>,
    ): LazyCacheHolder<Result, @ParameterName(name = "args") Args> {
        val token = object : TypeToken<Result>() {}
        val key = CacheKeyGenerator(token, customKey, gson).generatePrefix()
        return holders.getOrPut(key) {
            LazyCacheHolder(inMemoryCache, updater, customKey, token, gson) as LazyCacheHolder<Any, Any>
        } as LazyCacheHolder<Result, Args>
    }
}