package kekmech.ru.domain_schedule.network

import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.library_network.BackendServiceUrl
import kekmech.ru.library_network.EndpointUrl
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(BackendServiceUrl.SCHEDULE)
interface ScheduleService {

    @GET("v1/{type}/{name}/schedule/{weekOffset}")
    suspend fun getSchedule(
        @Path("type") type: String,
        @Path("name", encoded = true) name: String,
        @Path("weekOffset") weekOffset: Int
    ): Schedule

    @GET("v1/search")
    suspend fun getSearchResults(
        @Query("q", encoded = true) query: String,
        @Query("type") type: ScheduleType?
    ): GetSearchResultsResponse
}
