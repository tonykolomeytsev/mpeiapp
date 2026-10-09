package kekmech.ru.feature_schedule_impl.data.network.mpei

import kekmech.ru.lib_network.BackendServiceUrl
import kekmech.ru.lib_network.EndpointUrl
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(BackendServiceUrl.MPEI_TIMETABLE)
internal interface MpeiScheduleService {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("type") type: String? = null,
    ): List<MpeiSearchResultDto>

    @GET("schedule/{type}/{id}")
    suspend fun getSchedule(
        @Path("type") type: String,
        @Path("id") id: Long,
        @Query("start") start: String,
        @Query("finish") finish: String,
        @Query("lng") lng: Int = 1,
    ): List<MpeiClassesDto>
}
