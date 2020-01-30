package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.ChangeCurrentScheduleUseCase

class ChangeCurrentScheduleUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : ChangeCurrentScheduleUseCase {
    override suspend operator fun invoke(groupNumber: String) = scheduleRepository.addSchedule(groupNumber)
}