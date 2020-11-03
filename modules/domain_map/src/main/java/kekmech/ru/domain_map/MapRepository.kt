package kekmech.ru.domain_map

import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_cache.create
import kekmech.ru.common_persistent_cache.orFromPersistentCache

class MapRepository(
    private val mapService: MapService,
    private val mapPersistentCache: MapPersistentCache,
    lifetimeCache: LifetimeCache
) {
    private val mapMarkersLifetimeCache = lifetimeCache.create { mapService.getMapMarkers() }

    fun observeMarkers() = mapMarkersLifetimeCache.asSingle()
        .orFromPersistentCache(Unit, mapPersistentCache)
}