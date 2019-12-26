package kekmech.ru.domain

import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.SaveScheduleUseCase

class SaveScheduleUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : SaveScheduleUseCase {

    override operator fun invoke(schedule: Schedule) {
        scheduleRepository.saveSchedule(schedule)
    }

}