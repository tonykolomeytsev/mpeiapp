package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.in_memory_cache.InMemoryCache
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_di.AppCacheDir
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val CommonCacheModule = module {
    singleOf(::InMemoryCache)
    single { PersistentCache(get(), get<AppCacheDir>().dir) }
}
