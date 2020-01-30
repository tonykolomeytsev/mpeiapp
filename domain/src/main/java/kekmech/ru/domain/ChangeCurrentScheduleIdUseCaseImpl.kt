package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.ChangeCurrentScheduleIdUseCase

class ChangeCurrentScheduleIdUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : ChangeCurrentScheduleIdUseCase {
    override fun invoke(newCurrentId: Int) = scheduleRepository.setCurrentScheduleId(newCurrentId)
}