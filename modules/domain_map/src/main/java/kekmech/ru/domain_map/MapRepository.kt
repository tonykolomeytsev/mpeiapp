package kekmech.ru.domain_map

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.domain_map.dto.MapMarker

class MapRepository(
    private val mapService: MapService,
    persistentCache: PersistentCache,
) {

    private val mapMarkersCache =
        persistentCache.of(
            key = MAP_CACHE_KEY,
            valueClass = listOf<MapMarker>().javaClass,
        )

    fun getMarkers(): Single<List<MapMarker>> =
        mapService
            .getMapMarkers()
            .doOnSuccess(mapMarkersCache::set)
            .onErrorResumeWith(mapMarkersCache.get().toSingle())

    private companion object {

        const val MAP_CACHE_KEY = "MAP_CACHE_KEY"
    }
}