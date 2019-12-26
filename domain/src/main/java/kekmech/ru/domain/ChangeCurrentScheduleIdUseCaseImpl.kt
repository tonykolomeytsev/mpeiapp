package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.ChangeCurrentScheduleIdUseCase

class ChangeCurrentScheduleIdUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : ChangeCurrentScheduleIdUseCase {
    override fun invoke(newCurrentId: Int) = scheduleRepository.setCurrentScheduleId(newCurrentId)
}