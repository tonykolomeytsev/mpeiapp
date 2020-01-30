package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.InvokeUpdateScheduleUseCase

class InvokeUpdateScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository
) : InvokeUpdateScheduleUseCase {
    override suspend operator fun invoke() {
        scheduleRepository.syncronize()
    }
}