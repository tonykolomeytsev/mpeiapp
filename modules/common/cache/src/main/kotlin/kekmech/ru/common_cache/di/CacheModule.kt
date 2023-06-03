package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_di.AppCacheDir
import org.koin.dsl.module

@Deprecated("Use red_mad_robot MapMemory instead")
val CommonCacheModule = module {
    single { PersistentCache(get(), get<AppCacheDir>().dir) }
}
