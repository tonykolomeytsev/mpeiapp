package kekmech.ru.domain_schedule

import io.reactivex.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule.dto.GetSessionResponse
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.SearchResultType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(BackendServiceUrl.SCHEDULE)
interface ScheduleService {

    @GET("v1/{type}/{groupNumber}/schedule/{weekOffset}")
    fun getSchedule(
        @Path("type") scheduleType: String,
        @Path("groupNumber", encoded = true) groupNumber: String,
        @Path("weekOffset") weekOffset: Int
    ): Single<Schedule>

    @GET("v1/{type}/{groupNumber}/session")
    fun getSession(
        @Path("type") scheduleType: String,
        @Path("groupNumber", encoded = true) groupNumber: String
    ): Single<GetSessionResponse>

    @GET("v1/search")
    fun getSearchResults(
        @Query("q", encoded = true) query: String,
        @Query("type") type: SearchResultType?
    ): Single<GetSearchResultsResponse>
}