package kekmech.ru.common_cache.di

import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_di.AppCacheDir
import org.koin.dsl.module

val CommonCacheModule = module {
    single { PersistentCache(get(), get<AppCacheDir>().dir) }
}
