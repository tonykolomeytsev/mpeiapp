package kekmech.ru.domain

import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetAllSchedulesUseCase
import javax.inject.Inject

class GetAllSchedulesUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetAllSchedulesUseCase {
    override fun invoke() = scheduleRepository.getAllSchedules()
}