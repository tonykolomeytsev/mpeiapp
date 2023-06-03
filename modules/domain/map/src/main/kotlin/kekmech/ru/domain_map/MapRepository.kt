package kekmech.ru.domain_map

import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_api.ofList
import kekmech.ru.domain_map.dto.MapMarker

class MapRepository internal constructor(
    private val mapService: MapService,
    persistentCache: PersistentCache,
) {

    private val mapMarkersCache by persistentCache.ofList<MapMarker>()

    suspend fun getMarkers(): Result<List<MapMarker>> =
        runCatching { mapService.getMapMarkers() }
            .onSuccess { mapMarkersCache.put(ArrayList(it)) }
            .recoverCatching { mapMarkersCache.get().getOrThrow() }
}
