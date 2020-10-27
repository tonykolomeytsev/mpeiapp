package kekmech.ru.domain_map

import kekmech.ru.common_persistent_cache.orFromCache

class MapRepository(
    private val mapService: MapService,
    private val mapPersistentCache: MapPersistentCache
) {
    fun observeMarkers() = mapService.getMapMarkers()
        .orFromCache(Unit, mapPersistentCache)
}