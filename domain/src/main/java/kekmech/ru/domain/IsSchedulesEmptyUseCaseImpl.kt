package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.IsSchedulesEmptyUseCase

class IsSchedulesEmptyUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : IsSchedulesEmptyUseCase {
    override suspend operator fun invoke(): Boolean {
        return scheduleRepository.isSchedulesEmpty()
    }
}