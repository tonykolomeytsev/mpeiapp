package kekmech.ru.domain_dashboard.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_dashboard.dto.ScheduleMetaInfo
import kekmech.ru.domain_schedule.ScheduleRepository

class GetSelectedScheduleMetaInfoUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    fun getScheduleMetaInfo(): Single<ScheduleMetaInfo> =
        scheduleRepository
            .loadSchedule(weekOffset = 0)
            .map {
                ScheduleMetaInfo(
                    name = it.name,
                    type = it.type,
                    weekOfSemester = it.weeks.first().weekOfSemester,
                )
            }
}
