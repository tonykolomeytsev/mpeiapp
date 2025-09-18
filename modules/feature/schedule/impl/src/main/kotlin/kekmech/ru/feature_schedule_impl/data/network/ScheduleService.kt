package kekmech.ru.feature_schedule_impl.data.network

import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_impl.data.dto.GetSearchResultsResponse
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.lib_network.BackendServiceUrl
import kekmech.ru.lib_network.EndpointUrl
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(BackendServiceUrl.SCHEDULE)
internal interface ScheduleService {

    @GET("v1/{type}/{name}/schedule/{weekOffset}")
    suspend fun getSchedule(
        @Path("type") type: String,
        @Path("name", encoded = true) name: String,
        @Path("weekOffset") weekOffset: Int,
    ): ScheduleDto

    @GET("v1/search")
    suspend fun getSearchResults(
        @Query("q", encoded = true) query: String,
        @Query("type") type: ScheduleType?,
    ): GetSearchResultsResponse
}
