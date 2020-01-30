package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.GetAllSchedulesUseCase

class GetAllSchedulesUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : GetAllSchedulesUseCase {
    override fun invoke() = scheduleRepository.getAllSchedules()
}