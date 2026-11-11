package kekmech.ru.feature_map_api.data.repository

import kekmech.ru.feature_map_api.domain.model.MapMarker

public interface MapRepository {

    /**
     * Downloads the markers and saves them. If the download fails, returns the saved markers.
     */
    public suspend fun getMarkers(): Result<List<MapMarker>>

    /**
     * Markers saved by the last successful [getMarkers], without the network request.
     * Fails if the markers were never downloaded.
     */
    public suspend fun getSavedMarkers(): Result<List<MapMarker>>
}
