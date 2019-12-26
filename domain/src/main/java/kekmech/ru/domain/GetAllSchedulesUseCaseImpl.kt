package kekmech.ru.domain

import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetAllSchedulesUseCase

class GetAllSchedulesUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : GetAllSchedulesUseCase {
    override fun invoke() = scheduleRepository.getAllSchedules()
}