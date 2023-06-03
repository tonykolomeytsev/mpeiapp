package kekmech.ru.domain_map

import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_map.dto.MapMarker
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.MAP)
internal interface MapService {

    @GET("markers.json")
    suspend fun getMapMarkers(): List<MapMarker>
}
