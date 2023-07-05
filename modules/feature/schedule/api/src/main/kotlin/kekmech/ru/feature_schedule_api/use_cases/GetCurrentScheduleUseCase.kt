package kekmech.ru.feature_schedule_api.use_cases

import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Schedule

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
