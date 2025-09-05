package kekmech.ru.domain_schedule.network

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(BackendServiceUrl.SCHEDULE)
public interface ScheduleService {

    @GET("v1/{type}/{name}/schedule/{weekOffset}")
    public fun getSchedule(
        @Path("type") type: String,
        @Path("name", encoded = true) name: String,
        @Path("weekOffset") weekOffset: Int
    ): Single<Schedule>

    @GET("v1/search")
    public fun getSearchResults(
        @Query("q", encoded = true) query: String,
        @Query("type") type: ScheduleType?
    ): Single<GetSearchResultsResponse>
}
