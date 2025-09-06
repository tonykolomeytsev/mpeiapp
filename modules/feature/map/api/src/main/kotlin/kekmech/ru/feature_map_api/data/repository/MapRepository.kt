package kekmech.ru.feature_map_api.data.repository

import kekmech.ru.feature_map_api.domain.model.MapMarker

public interface MapRepository {

    public suspend fun getMarkers(): Result<List<MapMarker>>
}
