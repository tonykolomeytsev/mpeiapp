package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.IsSchedulesEmptyUseCase
import javax.inject.Inject

class IsSchedulesEmptyUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : IsSchedulesEmptyUseCase {
    override fun invoke(): Boolean {
        return scheduleRepository.isSchedulesEmpty()
    }
}