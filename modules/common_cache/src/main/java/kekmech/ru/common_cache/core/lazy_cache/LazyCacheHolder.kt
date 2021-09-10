package kekmech.ru.common_cache.core.lazy_cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_cache.core.in_memory_cache.CacheKeyGenerator
import kekmech.ru.common_cache.core.in_memory_cache.InMemoryCache
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds all instances of caches for the same [updater].
 *
 * What it it? Repository for a single request.
 *
 * The logic behind this is that every requests corresponds to multiple data values depending on request arguments.
 * So [LazyCacheHolder] keeps all caches for the same updater, but data for each request is kept inside [LazyCache]
 * depending on the arguments.
 *
 * For example, a Pocket details request has a `pocketId` argument. The same pocket details request correspond to
 * multiple actual pockets, each for it's own id.
 * So, [LazyCacheHolder] keeps all caches for the request "load pocket details". But every [LazyCache] inside it keeps
 * data for every `pocketId` that would be provided to this request.
 *
 * Taken from: https://github.com/vivid-money/lazycache
 */
open class LazyCacheHolder<Result : Any, Args : Any>(
    private val inMemoryCache: InMemoryCache,
    private val updater: (args: Args) -> Observable<Result>,
    customKey: String?,
    resultToken: TypeToken<Result>,
    gson: Gson,
) {

    private val keyGenerator = CacheKeyGenerator(resultToken, customKey, gson)
    private val caches = ConcurrentHashMap<Args, LazyCache<Result, Args>>()

    /**
     * Clears cached values in all caches
     */
    fun clear() {
        // Clear every entry instead of clearing the map to keep
        // active cache observers working
        for (entry in caches.entries) {
            entry.value.clear()
        }
    }

    fun getCache(args: Args): LazyCache<Result, Args> = caches.getOrPut(args) { createLazyCache(args) }

    fun getAllCached(): Collection<LazyCache<Result, Args>> = caches.values.filter { it.hasValue() }

    private fun createLazyCache(
        args: Args
    ) = LazyCache(inMemoryCache.of(keyGenerator.generate(args), false), args, updater)
}