package kekmech.ru.feature_map_impl.data.repository

import android.content.Context
import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.data.mapper.MapMarkerMapper
import kekmech.ru.feature_map_impl.data.model.MapMarkerDto
import kekmech.ru.feature_map_impl.data.network.MapService
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.ofList
import kotlinx.serialization.json.Json

internal class MapRepositoryImpl internal constructor(
    private val mapService: MapService,
    persistentCache: PersistentCache,
    private val context: Context,
) : MapRepository {

    private val mapMarkersCache by persistentCache.ofList<MapMarker>()
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getMarkers(): Result<List<MapMarker>> {
        val remoteResult = runCatching { mapService.getMapMarkers() }
            .map { MapMarkerMapper.dtoToDomain(it) }

        if (remoteResult.isSuccess) {
            val markers = remoteResult.getOrThrow()
            mapMarkersCache.put(ArrayList(markers))
            return Result.success(markers)
        }

        val cached = mapMarkersCache.get().getOrNull()
        if (cached != null) {
            return Result.success(cached)
        }

        val assetMarkers = loadFromAssets()
        if (assetMarkers != null) {
            mapMarkersCache.put(ArrayList(assetMarkers))
            return Result.success(assetMarkers)
        }

        return Result.failure(
            remoteResult.exceptionOrNull() ?: IllegalStateException("Failed to load map markers")
        )
    }

    private fun loadFromAssets(): List<MapMarker>? = runCatching {
        context.assets.open("markers.json").bufferedReader().use { reader ->
            val dtoList = json.decodeFromString<List<MapMarkerDto>>(reader.readText())
            MapMarkerMapper.dtoToDomain(dtoList)
        }
    }.getOrNull()
}
