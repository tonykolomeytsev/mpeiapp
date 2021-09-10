package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_cache.in_memory_cache.InMemoryCache
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_di.ModuleProvider
import org.koin.core.qualifier.named
import org.koin.dsl.bind

object CacheModule : ModuleProvider({
    single { LifetimeCache() } bind LifetimeCache::class
    single { InMemoryCache(get()) }
    single { PersistentCache(get(), get(named("appCacheDir"))) }
})