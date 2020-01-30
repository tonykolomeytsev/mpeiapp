package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.LoadNewScheduleUseCase

class LoadNewScheduleUseCaseImpl(
    private val scheduleRepository: OldScheduleRepository
) : LoadNewScheduleUseCase {
    override suspend fun invoke(groupNum: String) {
        val schedule = scheduleRepository.loadScheduleFromRemote(groupNum)
        scheduleRepository.updateScheduleByGroupNum(schedule)
    }
}