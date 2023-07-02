package kekmech.ru.domain_map

import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.library_network.BackendServiceUrl
import kekmech.ru.library_network.EndpointUrl
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.MAP)
internal interface MapService {

    @GET("markers.json")
    suspend fun getMapMarkers(): List<MapMarker>
}
