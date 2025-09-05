package kekmech.ru.domain_map

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.domain_map.dto.MapMarkersCacheWrapper

public class MapRepository(
    private val mapService: MapService,
    persistentCache: PersistentCache,
) {

    private val mapMarkersCache =
        persistentCache.of(
            key = MAP_CACHE_KEY,
            valueClass = MapMarkersCacheWrapper::class.java,
        )

    public fun getMarkers(): Single<List<MapMarker>> =
        mapService
            .getMapMarkers()
            .doOnSuccess { mapMarkersCache.set(MapMarkersCacheWrapper(it)) }
            .onErrorResumeWith(
                mapMarkersCache
                    .getOrError()
                    .map { it.mapMarkers }
            )

    private companion object {

        const val MAP_CACHE_KEY = "MAP_CACHE_KEY"
    }
}
