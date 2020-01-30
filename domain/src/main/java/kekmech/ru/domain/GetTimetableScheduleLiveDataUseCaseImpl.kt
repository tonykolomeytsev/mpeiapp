package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTimetableScheduleLiveDataUseCase

class GetTimetableScheduleLiveDataUseCaseImpl(
    private val scheduleRepository: ScheduleRepository
) : GetTimetableScheduleLiveDataUseCase {
    override fun invoke() = scheduleRepository.schedule
}