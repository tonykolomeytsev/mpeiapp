package kekmech.ru.domain_schedule

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule.dto.Schedule

interface ScheduleTransformer {
    fun transform(schedule: Schedule): Single<Schedule>
}