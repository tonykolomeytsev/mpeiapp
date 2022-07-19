package kekmech.ru.domain_map

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_map.dto.MapMarker
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.MAP)
interface MapService {

    @GET("markers.json")
    fun getMapMarkers(): Single<List<MapMarker>>
}