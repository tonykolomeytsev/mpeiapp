package kekmech.ru.domain_map

import io.reactivex.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_map.dto.GetMapMarkersResponse
import retrofit2.http.GET


@EndpointUrl(BackendServiceUrl.MAP)
interface MapService {

    @GET("getMapMarkers")
    fun getMapMarkers(): Single<GetMapMarkersResponse>
}