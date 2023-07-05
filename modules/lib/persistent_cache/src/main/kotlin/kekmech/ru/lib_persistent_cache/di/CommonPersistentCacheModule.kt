package kekmech.ru.lib_persistent_cache.di

import android.content.Context
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.impl.PersistentCacheImpl
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheDir
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheSource
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheSourceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

val LibraryPersistentCacheModule = module {
    factory { PersistentCacheDir(File(get<Context>().cacheDir, "persistent")) }
    factoryOf(::PersistentCacheSourceImpl) bind PersistentCacheSource::class
    singleOf(::PersistentCacheImpl) bind PersistentCache::class
}
