package kekmech.ru.domain

import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.SaveScheduleUseCase
import javax.inject.Inject

class SaveScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : SaveScheduleUseCase {

    override operator fun invoke(schedule: Schedule) {
        scheduleRepository.saveSchedule(schedule)
    }

}