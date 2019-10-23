package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.ChangeCurrentScheduleIdUseCase
import javax.inject.Inject

class ChangeCurrentScheduleIdUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ChangeCurrentScheduleIdUseCase {
    override fun invoke(newCurrentId: Int) = scheduleRepository.setCurrentScheduleId(newCurrentId)
}