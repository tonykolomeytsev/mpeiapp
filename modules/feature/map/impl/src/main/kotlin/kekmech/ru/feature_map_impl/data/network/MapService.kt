package kekmech.ru.feature_map_impl.data.network

import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.lib_network.BackendServiceUrl
import kekmech.ru.lib_network.EndpointUrl
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.MAP)
internal interface MapService {

    @GET("markers.json")
    suspend fun getMapMarkers(): List<MapMarker>
}
