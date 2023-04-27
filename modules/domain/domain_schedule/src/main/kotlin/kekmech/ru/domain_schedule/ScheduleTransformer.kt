package kekmech.ru.domain_schedule

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule_models.dto.Schedule

@Deprecated("This approach is deprecated, use `*UseCase` instead")
interface ScheduleTransformer {
    fun transform(schedule: Schedule): Single<Schedule>
}
