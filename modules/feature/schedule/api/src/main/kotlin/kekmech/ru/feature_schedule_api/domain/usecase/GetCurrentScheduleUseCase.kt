package kekmech.ru.feature_schedule_api.domain.usecase

import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Schedule

public class GetCurrentScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    public suspend fun getSchedule(weekOffset: Int): Schedule {
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
