package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.IsSchedulesEmptyUseCase

class IsSchedulesEmptyUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : IsSchedulesEmptyUseCase {
    override fun invoke(): Boolean {
        return scheduleRepository.isSchedulesEmpty()
    }
}