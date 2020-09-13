package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_di.ModuleProvider
import org.koin.dsl.bind

object CacheModule : ModuleProvider({
    single { LifetimeCache() } bind LifetimeCache::class
})