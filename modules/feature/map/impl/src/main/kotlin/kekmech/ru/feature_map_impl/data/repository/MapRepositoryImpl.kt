package kekmech.ru.feature_map_impl.data.repository

import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.data.mapper.MapMarkerMapper
import kekmech.ru.feature_map_impl.data.network.MapService
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.ofList

internal class MapRepositoryImpl internal constructor(
    private val mapService: MapService,
    persistentCache: PersistentCache,
) : MapRepository {

    private val mapMarkersCache by persistentCache.ofList<MapMarker>()

    override suspend fun getMarkers(): Result<List<MapMarker>> =
        runCatching { mapService.getMapMarkers() }
            .map { MapMarkerMapper.dtoToDomain(it) }
            .onSuccess { mapMarkersCache.put(ArrayList(it)) }
            .recoverCatching { mapMarkersCache.get().getOrThrow() }
}
