package kekmech.ru.domain_map

import kekmech.ru.common_cache.core.LifetimeCache
import kekmech.ru.common_cache.create

class MapRepository(
    private val mapService: MapService,
    lifetimeCache: LifetimeCache
) {
    private val markersCache = lifetimeCache.create { mapService.getMapMarkers() }

    fun observeMarkers() = markersCache.asObservable()
}