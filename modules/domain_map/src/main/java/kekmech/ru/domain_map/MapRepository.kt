package kekmech.ru.domain_map

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_cache.persistent_cache.getOrLoadFrom
import kekmech.ru.common_cache.persistent_cache.ofComposited
import kekmech.ru.domain_map.dto.GetMapMarkersResponse
import java.time.Duration

class MapRepository(
    private val mapService: MapService,
    persistentCache: PersistentCache,
) {

    private val mapMarkersCache =
        persistentCache.ofComposited(
            key = MAP_CACHE_KEY,
            valueClass = GetMapMarkersResponse::class.java,
            lifetime = Duration.ofDays(1L),
        )

    fun getMarkers(): Single<GetMapMarkersResponse> =
        mapMarkersCache.getOrLoadFrom(mapService.getMapMarkers())

    private companion object {

        const val MAP_CACHE_KEY = "MAP_CACHE_KEY"
    }
}