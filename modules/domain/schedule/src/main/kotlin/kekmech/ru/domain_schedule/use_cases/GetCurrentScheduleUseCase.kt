package kekmech.ru.domain_schedule.use_cases

import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.domain_schedule_models.dto.Schedule

class GetCurrentScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun getSchedule(weekOffset: Int): Schedule {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return scheduleRepository
            .getSchedule(
                type = selectedSchedule.type,
                name = selectedSchedule.name,
                weekOffset = weekOffset,
            )
            .getOrThrow()
    }
}
