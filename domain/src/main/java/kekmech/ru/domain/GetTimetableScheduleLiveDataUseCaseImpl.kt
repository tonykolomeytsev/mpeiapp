package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.GetTimetableScheduleLiveDataUseCase

class GetTimetableScheduleLiveDataUseCaseImpl(
    private val scheduleRepository: OldScheduleRepository
) : GetTimetableScheduleLiveDataUseCase {
    override fun invoke() = scheduleRepository.scheduleLiveData
}