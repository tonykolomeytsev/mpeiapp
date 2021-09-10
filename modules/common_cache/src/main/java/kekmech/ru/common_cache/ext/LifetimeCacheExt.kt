package kekmech.ru.common_cache.ext

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.CacheFacade
import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_cache.core.LifetimeCacheUnitImpl
import java.util.*

@Deprecated("Deprecated in MpeiX 1.9.0")
fun <T : Any> LifetimeCache.create(dataSource: () -> Single<T>): CacheFacade<T> =
    CacheFacade(
        LifetimeCacheUnitImpl(UUID.randomUUID().toString(), this)
    ) { dataSource().toObservable() }