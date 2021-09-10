package kekmech.ru.domain_map

import kekmech.ru.common_cache.core.in_memory_cache.InMemoryCache

class MapRepository(
    private val mapService: MapService,
    private val mapPersistentCache: MapPersistentCache,
    inMemoryCache: InMemoryCache,
) {
    private val mapMarkersLifetimeCache = inMemoryCache.of { mapService.getMapMarkers() }

    fun observeMarkers() = mapMarkersLifetimeCache.asSingle()
        .orFromPersistentCache(Unit, mapPersistentCache)
}