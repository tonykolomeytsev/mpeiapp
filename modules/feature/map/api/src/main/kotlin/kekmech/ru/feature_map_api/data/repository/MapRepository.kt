package kekmech.ru.feature_map_api.data.repository

import kekmech.ru.feature_map_api.domain.model.MapMarker

interface MapRepository {

    suspend fun getMarkers(): Result<List<MapMarker>>
}
