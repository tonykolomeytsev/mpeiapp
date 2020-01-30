package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.InvokeUpdateScheduleUseCase

class InvokeUpdateScheduleUseCaseImpl(
    private val scheduleRepository: OldScheduleRepository
) : InvokeUpdateScheduleUseCase {
    override suspend operator fun invoke() {
        val groupNum = scheduleRepository.getGroupNum().value
        if (groupNum != null) {
            val schedule = scheduleRepository.loadScheduleFromRemote(groupNum)
            scheduleRepository.updateScheduleByGroupNum(schedule)
        }
    }
}