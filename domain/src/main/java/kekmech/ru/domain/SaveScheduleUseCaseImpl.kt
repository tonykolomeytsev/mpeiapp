package kekmech.ru.domain

import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.SaveScheduleUseCase

class SaveScheduleUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : SaveScheduleUseCase {

    override operator fun invoke(schedule: Schedule) {
        scheduleRepository.saveSchedule(schedule)
    }

}