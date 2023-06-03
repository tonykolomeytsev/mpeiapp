package kekmech.ru.common_persistent_cache_impl.di

import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_impl.PersistentCacheDir
import kekmech.ru.common_persistent_cache_impl.PersistentCacheImpl
import kekmech.ru.common_persistent_cache_impl.data.PersistentCacheSource
import kekmech.ru.common_persistent_cache_impl.data.PersistentCacheSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

val CommonPersistentCacheModule = module {
    factory { PersistentCacheDir(File(androidApplication().cacheDir, "persistent")) }
    factoryOf(::PersistentCacheSourceImpl) bind PersistentCacheSource::class
    singleOf(::PersistentCacheImpl) bind PersistentCache::class
}
