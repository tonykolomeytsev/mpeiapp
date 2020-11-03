package kekmech.ru.domain_schedule

import io.reactivex.Single
import kekmech.ru.domain_schedule.dto.Schedule

interface ScheduleTransformer {
    fun transform(schedule: Schedule): Single<Schedule>
}