package kekmech.ru.domain_map

import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.library_persistent_cache.api.PersistentCache
import kekmech.ru.library_persistent_cache.api.ofList

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
