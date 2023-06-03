package kekmech.ru.domain_schedule.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule_models.dto.Schedule

class GetCurrentScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    fun getSchedule(weekOffset: Int): Single<Schedule> =
        scheduleRepository
            .getSelectedSchedule()
            .flatMap {
                scheduleRepository.getSchedule(
                    type = it.type,
                    name = it.name,
                    weekOffset = weekOffset,
                )
            }
}
