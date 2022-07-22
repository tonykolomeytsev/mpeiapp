package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.in_memory_cache.InMemoryCache
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_di.ModuleProvider
import org.koin.core.qualifier.named

object CacheModule : ModuleProvider({
    single { InMemoryCache(get()) }
    single { PersistentCache(get(), get(named("appCacheDir"))) }
})
