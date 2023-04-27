package kekmech.ru.domain_dashboard.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_dashboard.dto.ScheduleMetaInfo
import kekmech.ru.domain_schedule.ScheduleRepository

class GetSelectedScheduleMetaInfo(
    private val scheduleRepository: ScheduleRepository,
) {

    fun getScheduleMetaInfo(): Single<ScheduleMetaInfo> {
        TODO()
    }
}
