package kekmech.ru.domain_schedule

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_schedule.dto.GetScheduleBody
import kekmech.ru.domain_schedule.dto.Schedule
import retrofit2.http.Body
import retrofit2.http.POST

@EndpointUrl("https://api.kekmech.com/mpeix/")
interface ScheduleService {

    @POST("schedule/getGroupSchedule")
    fun getSchedule(
        @Body body: GetScheduleBody
    ): Single<Schedule>

}